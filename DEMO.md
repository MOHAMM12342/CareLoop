# CareLoop 3-Minute Stage Demo Script

This script walks judges and audience members through the core value proposition of CareLoop in under 3 minutes, highlighting the offline commitment loop, family escalation, and opportunistic verification.

---

### Step 0: The Setup (0:00 - 0:25)
1. **Show Phone Status:**
   * Pull down the Android notification shade and **toggle Airplane Mode ON** (Wi-Fi OFF, Cellular OFF).
   * Notice the persistent top banner in CareLoop:  
     `"Airplane Mode / Offline • Local Agent active"`.
2. **Pitch Line:**
   > *"Doctolib gets you to the doctor. CareLoop makes sure you follow through — and tells your family when you don't. Everything you see right now is running 100% offline on this device, with zero cloud dependency and AES-256 local database encryption."*

---

### Step 1: Ingest Health Events (0:25 - 0:50)
1. Tap the **Play/Demo icon** in the top bar to open the **Stage Demo Controller**.
2. Tap **"1. Load Sample Mock FHIR Events"**.
3. Return to the **Today Schedule** view.
4. **Show the 3 Ingested Commitments:**
   * **Medication:** `Take Kardegic 75mg` (Due at 08:00)
   * **Appointment:** `Cardiology Consultation - Dr. Martin` (Due at 14:30)
   * **Lab Test:** `Fasting Blood Test (INR / Lipid)` (Due in 3 days)
5. **Pitch Line:**
   > *"CareLoop transforms appointments, tests, and prescriptions into a unified commitment ledger. Notice that none of these are marked complete yet."*

---

### Step 2: Exact Alarm & High-Priority Notification (0:50 - 1:20)
1. Return to the Demo Controller and tap **"2. Fire Kardegic 75mg Alarm"**.
2. A high-priority system heads-up notification immediately fires with sound and vibration:
   * **Title:** `Time for: Take Kardegic 75mg`
   * **Actions:** `[Done ✓]` and `[Snooze 15m]`.
3. **Pitch Line:**
   > *"The app schedules precise system alarms using Android's exact alarm API. If permission was denied by the OS, our AlarmCapabilityNegotiator automatically falls back to WorkManager so reminders are never lost."*
4. **Fast-Confirmation Shortcut Demo (Alternative to logging in):**
   * Demonstrate the **Triple-Tap Screen Extension**: Tap the screen 3 times rapidly (or tap the Touch icon in the top bar).
   * Instantly, the **Giant Quick Commitment Checker** pops up over the screen without any login or menu navigation!
   * Shows the scheduled medicine in large type with the giant 130dp green button: **"YES, I TOOK IT ✓"**.
   * Voice TTS reads aloud: *"Quick Check: Did you take your Kardegic 75 milligrams?"*

---

### Step 3: The Money Moment — Caregiver Escalation (1:20 - 2:00)
1. **Do NOT tap Done or Snooze.** Let the grace period elapse.
2. Tap **"3. Simulate Unconfirmed Grace Period"**.
3. **Observe the Escalation Ladder in Action:**
   * **Tier 1 Voice Prompt:** Text-to-Speech speaks aloud offline:  
     *"Reminder. It is time for: Take Kardegic 75mg. Please confirm on your screen."*
   * **Tier 4 Offline Fallback:** Because the phone is in Airplane Mode, CareLoop automatically launches a pre-filled SMS to the primary caregiver (`Sarah (Daughter)`):  
     *"CareLoop Alert: I may have missed my scheduled 'Take Kardegic 75mg' at 08:00. Please check in with me."*
4. Navigate to **Caregiver Activity Timeline** to show the immutable encrypted audit log:  
   `"Tier 4 executed (Offline fallback): Pre-filled SMS intent prepared for Sarah (Daughter)."`
5. **Pitch Line:**
   > *"This is the money moment. When an elderly patient fails to take critical medication, CareLoop doesn't just sit there. It escalates to family through a platform-compliant ladder that works even without an internet connection."*

---

### Step 4: Opportunistic Verification & Health Record Sync (2:00 - 2:40)
1. In the Demo Controller, tap **"4. Simulate FHIR: Appointment Completed"**.
2. Return to the Today screen:
   * Observe the **Cardiology Consultation** automatically flips to **Done**.
   * A prominent green badge appears: **`Verified from health record ✓`**.
3. Tap **"5. Simulate FHIR: DiagnosticReport Ready"**.
   * The **Blood Test** commitment auto-closes as well.
4. **Pitch Line:**
   > *"This is why CareLoop is a commitment agent, not a calendar. When clinical records arrive from the lab or hospital, the agent verifies completion against the medical record and auto-resolves the commitment."*

---

### Step 5: Toggle Airplane Mode OFF & Conclude (2:40 - 3:00)
1. Turn **Airplane Mode OFF**.
2. Within seconds, the connectivity monitor updates the banner to:  
   `"Online • Cloud sync & verification active"`.
3. Tap the **Pharmacies** icon to show nearby pharmacies and benchmark medicine prices fetched from OpenStreetMap and cached locally.
4. **Closing Canonical Line:**
   > *"Doctolib owns access. We own follow-through. Offline by default. Family in the loop."*

