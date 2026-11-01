# CareLoop — Current Version Executive Summary & Feature Matrix

> **Core Vision**: *"Doctolib gets you to the doctor. CareLoop makes sure you follow through — and tells your family when you don't."*

CareLoop is an **offline-first, privacy-preserving health commitment agent** for elderly patients and their family caregivers. It bridges the gap between receiving a prescription or appointment and actually following through with it, escalating through a compliant ladder when critical actions are missed.

---

## 1. Executive Summary: What CareLoop Does

Unlike traditional pill-reminder apps that rely on passive alarms or unverified checkboxes, CareLoop acts as an **autonomous commitment agent**:

1. **Ingests Health Events**: Captures health tasks via on-device CameraX OCR, manual entry, or Ségur-compliant DMP FHIR records (*Mon espace santé*).
2. **Tracks Commitments as a State Machine**: Advances commitments through `UPCOMING` → `DUE` → `MISSED` → `ESCALATED` → `COMPLETED` (with `SNOOZED` and `CANCELLED` states).
3. **Guarantees Zero-Friction Patient Interaction**: Elderly patients can confirm doses in one tap via a persistent lockscreen bypass, a notification tile, or by triple-tapping anywhere on screen.
4. **Enforces a Deterministic Escalation Ladder (Tiers 0–4)**: Progressively notifies the patient, uses voice Text-to-Speech (TTS), pushes alerts to family caregivers via FCM, triggers Twilio cellular phone calls or SMS, and executes an offline fallback.
5. **Protects Patient Privacy (Strict Data Minimisation)**: Outbound alerts to family or carriers state only: `"[Name] has not confirmed an important reminder."` No medication names, dosages, or diagnoses ever leave the device.
6. **Runs a Bundled On-Device AI Agent**: Qwen 2.5 0.5B Instruct is packaged directly inside the APK for offline natural language conversation, operating strictly under the **"Rules Decide, an LLM Proposes"** safety paradigm.
7. **Reconciles with Official Health Records**: Background workers verify appointment attendance and pharmacy dispensations against mock DMP FHIR fixtures, automatically awarding a *"Verified from health record ✓"* badge.

---

## 2. Comprehensive Feature Matrix

### A. The Agent Loop (Perceive → Decide → Act → Verify)
- **Perceive (`HealthEventSource`)**:
  - `ManualEntrySource`: Fast large-button form for quick additions.
  - `OcrPrescriptionSource`: CameraX + ML Kit Text Recognition v2 extracting medication, dosage (`75mg`, `1000mg`), and form (`sachet`, `comprimé`).
  - `MockFhirSource`: Pre-bundled HL7 FHIR fixtures mirroring *Mon espace santé* / DMP APIs.
- **Decide (`CommitmentEngine` & `EscalationScheduler`)**:
  - Computes exact milestones: $T$ (due), $T+15\text{m}$ (re-notify), $T+30\text{m}$ (primary caregiver alert), $T+45\text{m}$ (secondary caregiver voice call).
  - Appointments: $T-24\text{h}$, $T-1\text{h}$, $T+2\text{h}$ attendance confirmation.
- **Act (`EscalationLadderManager` & Actions)**:
  - Heads-up system notifications with interactive "Taken ✓" and "Snooze" action buttons.
  - Offline Android `TextToSpeech` spoken alerts in clear, plain language.
  - Dispatches SMS or French voice calls (`<Say voice="alice" language="fr-FR">`) via backend.
- **Verify (`VerificationWorker`)**:
  - Periodic Android `WorkManager` job that queries the health record source, checks fulfillment evidence, and auto-completes commitments without requiring manual patient action.

---

### B. Bundled On-Device AI Agent (Qwen 2.5 0.5B Instruct INT4)
- **Zero-Download Architecture**:
  - The 469 MB INT4 model weights file (`qwen2.5-0.5b-instruct-int4.bin`) is bundled directly in the APK's `assets/models/` folder.
  - Configured with `aaptOptions.noCompress` for zero-copy direct memory-mapping (`mmap`).
- **"Rules Decide, an LLM Proposes" Invariant**:
  - The model **only** interacts via typed tools (`propose_commitment`, `mark_taken`, `snooze`, `get_today`, `get_medications`, `draft_caregiver_message`).
  - **Human-in-the-Loop Safety Gate**: Any medication or schedule proposed by the model returns `PendingConfirmation`. Medical data is **never written to the encrypted database** until the patient explicitly taps "Confirm ✓" on the screen.
  - **MDR / Medical Device Safety**: Strictly refuses diagnostic or dosage advice (*"Can I double my dose?"* $\to$ *"I cannot give medical advice. Please contact your doctor or pharmacist."*).
  - **Deterministic Fallback**: If running on low-spec hardware, `CareLoopAgentEngine` falls back to its deterministic rule engine without crashing.

---

### C. Elderly-First Design & Accessibility
- **Zero Hidden Gestures**: No swipe-to-delete, no long-presses, no hidden drawer menus.
- **WCAG AAA Compliance**: Minimum 64dp touch targets, high contrast (`DeepNavy` `#0F1E36`, `PrimaryBlue` `#1565C0`, `HighContrastGreen` `#1B5E20`), and large typography (≥18sp body).
- **Triple-Tap Quick Commitment Checker**:
  - Rapidly tapping the screen 3 times opens `QuickCheckerActivity`.
  - Configured with `showWhenLocked="true"` and `turnScreenOn="true"` so seniors can confirm a dose without typing a device passcode.
  - Features a giant 130dp **"YES, I TOOK IT ✓"** button with tactile haptic and voice confirmation.
- **Quick Settings Tile (`QuickCheckerTileService`)**:
  - Allows 1-tap dose confirmation directly from the Android system notification shade.

---

### D. Compliance & Privacy by Design
- **Encrypted Storage**: Room database encrypted with **SQLCipher (AES-256)** with keys managed via Android `MasterKey` / Keystore.
- **Exact Alarms Policy (Android 14+)**: `AlarmCapabilityNegotiator` tests `canScheduleExactAlarms()`. If granted, sets `setExactAndAllowWhileIdle()`; if denied, falls back to `WorkManager` and presents a plain-language explanation dialog. `USE_EXACT_ALARM` removed to adhere to Google Play policies.
- **Dual Build Flavors**:
  - `demo`: Sideloaded APK for live hackathon evaluation. Sends silent background SMS via `SmsManager` under runtime `SEND_SMS` consent.
  - `play`: Google Play store compliant. Uses pre-filled `Intent.ACTION_SENDTO` (1-tap user confirmation) without requesting restricted permissions.
- **Stateless Backend Service (`backend/`)**:
  - Minimal Node.js service forwarding alerts to Twilio (SMS / Voice) and FCM (push notifications).
  - Contains **zero databases**, persists zero patient names or health data, and operates under French HDS (Art. L.1111-8) and GDPR Data Processing Agreement (DPA) requirements.

---

## 3. Technology Stack Reference

| Layer | Technologies |
| :--- | :--- |
| **Mobile Client** | Kotlin, Jetpack Compose, Material 3 Large-Type Theme, Navigation Compose |
| **Local Persistence** | Android Room 2.6.1 + SQLCipher (AES-256) |
| **Background & Alarms** | Android `AlarmManager` + `WorkManager` (2.9.0) |
| **On-Device Vision / OCR** | CameraX + Google ML Kit Text Recognition v2 + Local Regex Dictionary |
| **On-Device LLM** | Qwen 2.5 0.5B Instruct (Mobile INT4 Q4_K_M, 469 MB bundled in APK assets via `mmap`) |
| **Audio** | Android Native `TextToSpeech` (offline French/English synthesis) |
| **Network & Location** | Ktor Client (CIO Engine, Kotlinx Serialization), FusedLocationProvider + OpenStreetMap Overpass API |
| **Escalation Backend** | Node.js, Express, Twilio SDK (SMS & Voice TwiML), HDS/DPA compliant forwarder |

---

## 4. Verification & Testing Status

- **Android Unit Tests**: **32/32 tests passing** (`./gradlew testDemoDebugUnitTest testPlayDebugUnitTest`):
  - Database schema & state machine invariants (`DatabaseSchemaTest.kt`)
  - Alarm capability negotiation & action handling (`AlarmCapabilityAndActionsTest.kt`)
  - Escalation ladder milestone calculation & data minimisation (`EscalationLadderAndAuditTest.kt`)
  - Caregiver ranking, pairing tokens & GDPR consent (`CaregiverSetupAndConsentTest.kt`)
  - Automatic FHIR health record verification (`FhirVerificationWorkerTest.kt`)
  - Prescription OCR regex & dictionary matching (`PrescriptionOcrParserTest.kt`)
  - Policy Layer safety gates & bounded snoozes (`AgentPolicyLayerTest.kt`)
  - Qwen ChatML formatting & diagnostic refusal (`CareLoopAgentEngineTest.kt`)
- **Backend Tests**: **5/5 tests passing** (`npm test` in `backend/`):
  - Outbound message sanitization & drug keyword stripping
  - `/health` HDS compliance verification
  - Twilio simulation mode dispatch
  - Stateless FCM relay push forwarding
- **Ready Artifacts**:
  - `app/build/outputs/apk/demo/debug/app-demo-debug.apk` (503 MB standalone APK with Qwen 2.5 0.5B bundled)
  - `app/build/outputs/apk/play/debug/app-play-debug.apk` (503 MB standalone APK)

