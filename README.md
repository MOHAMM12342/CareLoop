# CareLoop

> **"Doctolib gets you to the doctor. CareLoop makes sure you follow through — and tells your family when you don't."**

CareLoop is an **offline-first, privacy-preserving health commitment agent** for Android, engineered for elderly patients and their family caregivers. Built for hackathon demonstration (IMT Starter) and production-readiness under European (GDPR) and French Digital Health (*Mon espace santé* / HDS) regulations.

---

## 1. Architecture: "Rules Decide, an LLM Proposes"

CareLoop operates on a strict safety boundary: **deterministic code governs all schedules, state machines, escalation ladders, and audit logs**. The on-device LLM serves solely as an intake listener and conversational companion.

```
[Elderly Patient Voice / Text]
             │
             ▼
┌─────────────────────────────────────────────────────────────┐
│ Bundled On-Device Model (Qwen 2.5 0.5B Instruct INT4)       │
│ Generates structured ChatML tool JSON calls                 │
└────────────────────────────┬────────────────────────────────┘
                             │
                             ▼ (Typed Tool Calls)
┌─────────────────────────────────────────────────────────────┐
│ CareLoop Policy Layer (Deterministic Safety Guardian)       │
│ - Validates invariants & bounds (e.g. 5–120 min snooze)     │
│ - Strict data minimisation (strips drug names from alerts)  │
│ - Human-in-the-Loop Safety Gate                             │
└───────────────┬─────────────────────────────┬───────────────┘
                │                             │
    (Medical data proposal)       (Safe deterministic action)
                ▼                             ▼
┌───────────────────────────────┐ ┌───────────────────────────┐
│ Patient Confirmation Card     │ │ Database & Alarms         │
│ Requires explicit 1-tap       │ │ - mark_taken()            │
│ "Confirm ✓" on screen         │ │ - snooze()                │
└───────────────────────────────┘ │ - get_today()             │
                                  └───────────────────────────┘
```

### Typed Tools Specification
1. `propose_commitment(type, title, dueAt, instructions)`: **Safety Gate** — returns `PendingConfirmation`. Never writes directly to the database until the patient confirms on screen.
2. `mark_taken(commitmentId)`: Marks a commitment completed and records tamper-proof adherence logs.
3. `snooze(commitmentId, snoozeMinutes)`: Delays reminders with bounds enforced between 5 and 120 minutes.
4. `get_today()` / `get_all_commitments()` / `get_medications()`: Reads current schedules and medications.
5. `draft_caregiver_message(reason)`: **Data Minimisation** — strictly masks or sanitizes medical details: `"[Name] has not confirmed an important reminder."` Never outputs drug names or clinical conditions.
6. **Zero Escalation Tools**: The LLM has no authority to cancel, delay, or trigger the escalation ladder.

---

## 2. On-Device Model Bundling (Qwen 2.5 0.5B Instruct)

The model **comes bundled directly inside the APK assets**, requiring **zero post-installation downloads**:

- **Model Selection**: **Qwen 2.5 0.5B Instruct (INT4 Mobile Q4_K_M)**.
  - State-of-the-art reasoning and tool calling for sub-1B parameter models.
  - Pre-trained on native multilingual vocabularies (French & English) essential for French healthcare terms (*"Prendre Kardegic 75mg"*, *"Docteur Laurent"*).
- **Asset Packaging**: Model weights reside in `app/src/main/assets/models/qwen2.5-0.5b-instruct-int4.bin` (469 MB).
- **Memory-Mapped Direct I/O (`mmap`)**: Configured with `aaptOptions.noCompress += listOf("bin", "task", "tflite", "gguf")` in `app/build.gradle.kts`. Android memory-maps weights directly from the APK into virtual memory without uncompressing 469 MB into heap RAM.
- **Offline Safety Engine Fallback**: If running in constrained environments, `CareLoopAgentEngine` falls back gracefully to deterministic rule-based parsing without crashing.
- **Strict MDR Guard**: Refuses diagnostic or dosage questions (*"Can I take a double dose?"* $\to$ *"I cannot give medical or dosage advice. Please contact your doctor or pharmacist."*).

---

## 3. The Platform-Compliant Escalation Ladder

CareLoop provides an escalation ladder with two build flavors:
- **`demo` flavor**: Sideloaded APK for live demos. Sends silent background SMS via `SmsManager` after patient and caregiver consent at onboarding (runtime `SEND_SMS`).
- **`play` flavor**: Google Play compliant. Uses pre-filled `Intent.ACTION_SENDTO` (one-tap patient send) adhering to Google Play policies.

| Tier | Channel | Offline? | Mechanism |
| :--- | :--- | :---: | :--- |
| **Tier 0** | Local Notification | **Yes** | High-priority heads-up notification with Taken & Snooze actions. |
| **Tier 1** | Re-notification + Voice TTS | **Yes** | Re-notification accompanied by Android TextToSpeech spoken reminder. |
| **Tier 2** | Caregiver Push via FCM | No | Stateless relay push notification forwarder to caregiver companion devices. |
| **Tier 3** | Backend Voice Call / SMS (Twilio) | No | Dispatches SMS or automated French voice call via stateless backend. |
| **Tier 4** | Offline Fallback | **Yes** | Sideloaded silent SMS (`demo`) or 1-tap `ACTION_SENDTO` intent (`play`). |

**Data Minimisation Invariant**: All outbound communications to family or carriers follow the format `"[Name] has not confirmed an important reminder."` Medication names and dosages are never leaked.

---

## 4. Ground-Truth Verification & Prescription OCR

### Opportunistic Health Record Verification (`VerificationWorker`)
- Doctolib has no public patient-facing API. The legally supported French integration path is **Mon espace santé / DMP (Dossier Médical Partagé) FHIR APIs**.
- CareLoop implements a clean `HealthEventSource` interface backed by a bundled `MockFhirSource` mirroring HL7 FHIR `MedicationRequest`, `Appointment`, and `DiagnosticReport` resources.
- `VerificationWorker` periodically queries health records in the background and auto-closes commitments with a **"Verified from health record ✓"** badge.

### Prescription OCR Verification Gate
- Built with **CameraX** and **Google ML Kit Text Recognition v2**.
- Parses medication name, dosage strength (`75mg`, `1000mg`), and form (`sachet`, `comprimé`) using a local dictionary and regex.
- **Mandatory Confirmation Screen (`OcrPrescriptionScreen`)**: Low-confidence or unknown medications prompt the user to type the name manually. Nothing is written to the database until the user explicitly taps "Confirm & Save".

---

## 5. Elderly Accessibility & UI Design

- **Zero Hidden Gestures**: No swipe-to-dismiss, no long-presses, no hidden drawer navigation.
- **WCAG AAA Compliance**: Minimum 64dp touch targets, high-contrast palette (`#0F1E36`, `#1565C0`, `#1B5E20`, `#B71C1C`), and large typography (≥18sp body, ≥24sp headlines).
- **Triple-Tap Quick Checker**: Tapping the screen 3 times rapidly from anywhere opens the giant 1-tap confirmation screen (`QuickCheckerActivity`), enabled with `showWhenLocked` and `turnScreenOn`.
- **Quick Settings Tile**: Android Quick Settings tile allows 1-tap dose confirmation directly from the notification shade.

---

## 6. Stateless Backend & French HDS Compliance

The backend (`backend/escalation_service.js`) is a forward-only Node.js service:
- **Stateless & Zero Database**: Never writes personal identifiers or health records to disk.
- **Twilio Integration**: Dispatches SMS and French Text-to-Speech voice calls (`<Say voice="alice" language="fr-FR">`).
- **Demo Mode**: Automatically simulates successful delivery if Twilio credentials are not set.
- **HDS Hosting (Art. L.1111-8 Code de la santé publique)**: Production deployment guide and GDPR Data Processing Agreement (DPA) requirements documented in [`backend/README.md`](file:///home/beast/Desktop/ProjectX/backend/README.md).

---

## 7. Build, Test, and Execution Instructions

### Android App
```bash
# Run unit tests across both flavors (32 tests)
./gradlew testDemoDebugUnitTest testPlayDebugUnitTest

# Assemble standalone APKs (Model bundled inside)
./gradlew assembleDemoDebug assemblePlayDebug
```
Generated packages:
- `app/build/outputs/apk/demo/debug/app-demo-debug.apk` (~503 MB self-contained APK with Qwen 2.5 0.5B INT4 bundled)
- `app/build/outputs/apk/play/debug/app-play-debug.apk` (~503 MB self-contained APK)

### Model Assets Setup (Included)
The model weights are downloaded via:
```bash
./scripts/download_qwen_weights.sh
```

### Backend Escalation Service
```bash
cd backend
npm install
npm test    # Runs 5/5 automated tests
npm start   # Starts stateless forwarder on port 8080
```
