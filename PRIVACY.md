# CareLoop Privacy & Regulatory Compliance Document

## 1. Privacy by Default & Data Minimization
CareLoop operates on a strict **zero-knowledge, offline-first** principle:
- **Encrypted at Rest:** All prescriptions, scheduled commitments, adherence history, and caregiver contact details reside exclusively in a local Room database encrypted with **SQLCipher (AES-256-CBC)**.
- **Master Key Security:** The database passphrase is generated using cryptographically secure random bytes and protected by the Android Keystore system (`MasterKeys.AES256_GCM_SPEC`).
- **No Cloud Synchronization in MVP:** CareLoop does not transmit personal health data to a centralized server. There are no tracking SDKs, no behavioral telemetry, and no ad identifiers.

---

## 2. GDPR Compliance & Caregiver Consent Flow
Under the General Data Protection Regulation (GDPR) Article 6 and Article 9 (Special Category Health Data):
1. **Explicit Caregiver Consent:** Before any caregiver or sibling is registered into the escalation chain, their explicit consent must be recorded.
2. **Audit Timestamp (`consentGrantedAt`):** The app records the exact epoch timestamp when consent was confirmed.
3. **Right to Erasure (Article 17):** Users can wipe all local records instantly via the **Delete All Data** button in Settings.
4. **Data Portability (Article 20):** An in-app JSON export function allows patients or authorized family members to export their full commitment ledger.

---

## 3. Human-in-the-Loop Safety Gate (OCR Ingestion)
- Under medical device safety principles, automated OCR transcription of handwritten or printed prescriptions carries risks of hallucination or misinterpreted dosages.
- **Mandatory Patient Confirmation:** CareLoop forbids direct database writes from OCR output. Every parsed prescription is presented on a high-contrast confirmation screen with editable fields.
- **Low Confidence Warnings:** If a medication name is not matched against the local dictionary or confidence falls below 70%, CareLoop prominently warns the user and requires manual verification.

---

## 4. HDS (Hébergeur de Données de Santé) Posture
Under French Law (Article L. 1111-8 of the Public Health Code), any service hosting personal health data must be certified by an approved HDS body:
- **Stateless Escalation Forwarder:** The CareLoop escalation backend is strictly **stateless**. It accepts an outgoing notification request, forwards the SMS/Call request to Twilio, and drops the payload immediately.
- **No Health Records on the Gateway:** The escalation payload contains only necessary check-in context (e.g. *"Please check in with your family member regarding their 08:00 commitment"*) and never exposes clinical dossiers, diagnostic notes, or historical adherence graphs.
- **Production Target:** In commercial production, the backend container must be hosted on an HDS-certified cloud instance (e.g., OVHcloud Healthcare, AWS Paris HDS zone, or Scaleway HDS) under a compliant Data Processing Agreement (DPA).

---

## 5. Regulatory Notice: Not a Medical Device
CareLoop is a personal commitment and compliance tracker. It does **not** provide clinical diagnosis, does not suggest dosages, does not check drug-drug interactions, and does not evaluate laboratory test outcomes. It displays a mandatory disclaimer to this effect on first launch and within Settings.

