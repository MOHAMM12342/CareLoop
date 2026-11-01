# CareLoop

> **"Doctolib gets you to the doctor. CareLoop makes sure you follow through — and tells your family when you don't."**

CareLoop is an **offline-first, privacy-preserving mobile health commitment agent** for Android (with future iOS support), engineered specifically for elderly patients and their family caregivers.

---

## 1. Vision & Architecture Overview

CareLoop is **not** a pill reminder. It is an autonomous on-device **commitment agent**:
- It ingests health events (prescriptions via on-device OCR, appointments, lab tests).
- Converts them into tracked commitments governed by a rigorous state machine (`UPCOMING` → `DUE` → `MISSED` → `ESCALATED` → `COMPLETED`).
- Prompts the patient with high-contrast, large touch-target UI (≥64dp) and offline Text-to-Speech (TTS).
- Opportunistically verifies completion against health records (Mon espace santé / DMP FHIR fixtures) when online, tagging items with a **"Verified from health record ✓"** badge.
- Escalates through a platform-compliant ladder to siblings/caregivers when commitments slip.

### The Commitment Loop
```
PERCEIVE ────► DECIDE ────► ACT ────► VERIFY
  │               │           │         │
  ├─ OCR Scan     ├─ State    ├─ Exact  └─ Online FHIR
  ├─ Mock FHIR       Machine     Alarm     Record Check
  └─ Manual       └─ Ladder   ├─ TTS       (Auto-close)
                     Schedule ├─ Tiered
                              │  Escalation
                              └─ Triple-Tap Quick Checker
                                 (Zero-Friction Confirmation)
```

---

## 2. Zero-Friction Commitment Checker (Elderly Multi-Tap Extension)

Elderly patients often struggle with typing passcodes, authenticating, or navigating hierarchical mobile menus. To eliminate all friction:
- **Triple-Tap Screen Shortcut (`multiTapDetector`):** Tapping the screen 3 times rapidly from anywhere opens the **Quick Commitment Checker** immediately.
- **Lockscreen / Ambient Bypass (`QuickCheckerActivity`):** Configured with `showWhenLocked="true"` and `turnScreenOn="true"` so the patient can confirm their dose without unlocking the device.
- **Single Giant Action Button (130dp):** Prominently displays the scheduled dose in high-contrast text with a massive **"YES, I TOOK IT ✓"** button, triggering instant haptic feedback, offline voice confirmation, alarm cancellation, and ledger audit logging.
- **Android Quick Settings Tile (`QuickCheckerTileService`):** Patients can also swipe down from the notification drawer on any screen and tap the "CareLoop Check" tile with 1 tap.

---

## 2. The Honest Integration Story

### No Doctolib Public API
Doctolib has **no public patient-facing API**. Any app claiming a direct "Doctolib API integration" is misleading judges or using brittle, fragile web scrapers that violate terms of service and breach patient privacy.

### The French Digital Health Path (Mon espace santé / DMP)
The official, secure, and legally supported integration path in France is the **Mon espace santé / DMP (Dossier Médical Partagé) FHIR APIs**, which Doctolib and medical software already feed via Ségur du Numérique standards.

For the MVP, CareLoop integrates via the clean `HealthEventSource` interface backed by a bundled `MockFhirSource` JSON fixture mirroring HL7 FHIR `MedicationRequest`, `Appointment`, and `DiagnosticReport` schemas. In production, this swaps directly to the authenticated DMP FHIR endpoint.

---

## 3. Platform-Compliant Escalation Ladder

Silent background SMS sending (`SEND_SMS`) will be **rejected by the Google Play Store** for non-default SMS handler apps, and iOS strictly prohibits background SMS. CareLoop solves this with a compliant 5-tier ladder:

| Tier | Channel | Offline? | Mechanism |
| :--- | :--- | :---: | :--- |
| **Tier 0** | Local Alarm & Notification | **Yes** | High-priority alarm sound, persistent vibration, heads-up display with "Done ✓" and "Snooze" actions. |
| **Tier 1** | Local Re-notification + Voice TTS | **Yes** | Fires after the grace period; Android TTS reads the reminder aloud in plain language. |
| **Tier 2** | Caregiver Push Notification | No | Dispatched via FCM if patient and caregiver devices are online. |
| **Tier 3** | Backend Voice Call / SMS (Twilio) | No | Patient app posts to a stateless, HDS-compliant backend that triggers a cellular phone call or SMS to the primary caregiver. |
| **Tier 4** | Platform-Compliant Offline Fallback | **Yes** | Pre-fills native `Intent.ACTION_SENDTO` with emergency text to the sibling, requiring a single user tap. Compliant because the send is user-initiated. |

All escalation steps are logged into an encrypted, tamper-proof on-device ledger visible in the **Caregiver Activity Timeline**.

---

## 4. Platform Capability Negotiation

Android 12+ (API 31+) restricts exact alarms (`SCHEDULE_EXACT_ALARM`). CareLoop implements `AlarmCapabilityNegotiator`:
- At startup, tests `AlarmManager.canScheduleExactAlarms()`.
- **Granted:** Schedules exact wakeup alarms (`setExactAndAllowWhileIdle`).
- **Denied:** Gracefully falls back to `WorkManager` (inexact ±15m window) and displays a plain-language dialog directing the user to system settings to grant permission for life-critical timing. CareLoop **never crashes or silently drops alarms**.

---

## 5. Technology Stack

- **UI:** Kotlin, Jetpack Compose, Material 3 with High Contrast & Large-Type Theme (≥64dp touch targets).
- **Database:** Room + SQLCipher (AES-256 encrypted at rest).
- **Scheduling:** `AlarmManager` with `AlarmCapabilityNegotiator` + `WorkManager` fallback.
- **Vision / OCR:** Google ML Kit Text Recognition v2 (offline, on-device) + local medication dictionary matcher.
- **Network & Location:** Ktor Client (CIO), `ConnectivityMonitor` (hardware connection vs. internet reachability), FusedLocationProvider + OpenStreetMap Overpass API (free, non-metered).
- **Audio:** Offline Android `TextToSpeech` engine.
- **Backend:** Node.js/Express stateless Twilio forwarder (HDS deployable).

