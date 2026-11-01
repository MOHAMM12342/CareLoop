# CareLoop Stateless Escalation Backend

This repository component implements the minimal, forward-only, stateless backend service for CareLoop's escalation ladder (Tier 2 FCM Push & Tier 3 Twilio SMS/Voice Call).

---

## 1. Architectural Principles & Data Minimisation

CareLoop operates on an **offline-first, zero-knowledge backend architecture**:
- **Stateless & Forward-Only**: The backend contains **no database**, writes no logs containing personal health identifiers, and retains zero records across restarts.
- **Strict Data Minimisation**: In compliance with the Core CareLoop Rules, outbound messages dispatched to caregivers or third-party telecom operators **never contain medication names, dosages, or clinical diagnoses**.
  - Standard format: `"[Name] has not confirmed an important reminder. Please check in with them."`
  - Sanitization logic strips any clinical keywords and enforces data-minimised templates before transmission.
- **Fail-Safe Simulation**: In hackathon and local demo environments without Twilio API keys, the service automatically switches to simulated mode and returns mock delivery identifiers (`sim-timestamp`).

---

## 2. Regulatory Compliance & Hosting Requirements

### A. French HDS Certification (*Hébergeur de Données de Santé*)
Under Article **L.1111-8** of the French Public Health Code (*Code de la santé publique*), any cloud infrastructure hosting, processing, or transmitting personal health data collected during prevention, diagnosis, care, or social/medico-social follow-up must be certified HDS.
- **Target Deployment**: Production instances must be deployed on an HDS-certified infrastructure provider (e.g., OVHcloud Healthcare, AWS Europe Paris HDS-certified regions, Scaleway Health, or Microsoft Azure France Central).
- **Scope of Processing**: Since CareLoop's backend only receives a forward request containing a contact number and a generic reminder alert, it reduces the HDS exposure perimeter (Activities 1 to 6 under AFNOR HDS V2).

### B. GDPR Data Processing Agreement (DPA)
Under GDPR Article 28:
- Any commercial deployment involving third-party telecom carriers (Twilio, Sinch, Orange API) must execute a formal **Data Processing Agreement (DPA)** with Standard Contractual Clauses (SCCs).
- Twilio operates under HIPAA-eligible architectures and EU BSR/SCC provisions.
- Zero health data is persisted on disk or transmitted in cleartext.

---

## 3. API Specification

### `GET /health`
Returns service availability, stateless verification, and compliance status.
```json
{
  "status": "ok",
  "service": "CareLoop Stateless Escalation Backend",
  "hdsCompliant": true,
  "stateless": true,
  "dataMinimised": true
}
```

### `POST /api/escalate`
Dispatches an urgent check-in SMS or automated Voice Call to the primary family caregiver via Twilio (or simulation).

**Request Body:**
```json
{
  "caregiverName": "Claire Dupont",
  "caregiverPhone": "+33612345678",
  "patientName": "Jean Dupont",
  "commitmentTitle": "an important reminder",
  "channel": "SMS" // or "VOICE_CALL"
}
```

**Response Body:**
```json
{
  "success": true,
  "simulated": true,
  "messageId": "sim-1730469448000",
  "channel": "SMS",
  "outboundMessage": "Jean Dupont has not confirmed an important reminder."
}
```

### `POST /api/fcm/relay`
Stateless forwarder for Firebase Cloud Messaging (FCM) high-priority push notifications to paired caregiver companion devices.

**Request Body:**
```json
{
  "pairingToken": "PAIR-98765",
  "caregiverFcmToken": "fcm-registration-token",
  "patientName": "Jean Dupont",
  "alertMessage": "Missed reminder",
  "priority": "HIGH"
}
```

**Response Body:**
```json
{
  "success": true,
  "relayed": true,
  "pairingToken": "PAIR-98765",
  "relayMessageId": "fcm-relay-1730469448000",
  "outboundMessage": "Jean Dupont has not confirmed an important reminder."
}
```

---

## 4. Local Setup & Running Instructions

### Prerequisites
- Node.js >= 18.x (Tested with Node v22)
- npm >= 9.x

### Run Locally
```bash
cd backend
npm install
npm test
npm start
```
The server binds to port `8080` (or the port defined by `PORT` environment variable).

### Configure Twilio (Optional)
If live SMS or Voice Call dispatch is desired, export the following environment variables:
```bash
export TWILIO_ACCOUNT_SID="ACxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
export TWILIO_AUTH_TOKEN="your_auth_token"
export TWILIO_FROM_NUMBER="+12025550199"
npm start
```
Without these credentials, the service automatically runs in simulated dispatch mode.

### Docker Deployment
```bash
docker build -t careloop-backend .
docker run -p 8080:8080 careloop-backend
```

