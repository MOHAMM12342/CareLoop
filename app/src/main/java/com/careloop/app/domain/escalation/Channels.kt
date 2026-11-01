package com.careloop.app.domain.escalation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.telephony.SmsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Demo flavor channel: Silent SMS delivery via SmsManager (requires runtime SEND_SMS consent).
 * Sideloaded APK behavior for offline elderly emergency escalation.
 */
class DemoSmsChannel(private val context: Context) : EscalationChannel {
    override val channelName: String = "DEMO_SMS_MANAGER"

    override suspend fun sendEmergencyAlert(caregiverPhone: String, patientName: String): EscalationResult = withContext(Dispatchers.IO) {
        val minimisedMessage = EscalationChannel.buildDataMinimisedMessage(patientName)
        try {
            val smsManager: SmsManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                context.getSystemService(SmsManager::class.java)
            } else {
                @Suppress("DEPRECATION")
                SmsManager.getDefault()
            }

            smsManager.sendTextMessage(caregiverPhone, null, minimisedMessage, null, null)
            EscalationResult(
                success = true,
                channelType = channelName,
                message = minimisedMessage
            )
        } catch (se: SecurityException) {
            EscalationResult(
                success = false,
                channelType = channelName,
                message = minimisedMessage,
                error = "SEND_SMS permission not granted at runtime: ${se.message}"
            )
        } catch (e: Exception) {
            EscalationResult(
                success = false,
                channelType = channelName,
                message = minimisedMessage,
                error = e.message ?: "Failed to dispatch SMS via SmsManager"
            )
        }
    }
}

/**
 * Play flavor channel: User-initiated pre-filled SMS intent (Intent.ACTION_SENDTO).
 * Strictly compliant with Google Play Store SMS/Call log restrictions.
 */
class PlaySmsChannel(private val context: Context) : EscalationChannel {
    override val channelName: String = "PLAY_SMS_INTENT"

    override suspend fun sendEmergencyAlert(caregiverPhone: String, patientName: String): EscalationResult = withContext(Dispatchers.Main) {
        val minimisedMessage = EscalationChannel.buildDataMinimisedMessage(patientName)
        try {
            val smsIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("smsto:$caregiverPhone")
                putExtra("sms_body", minimisedMessage)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(smsIntent)
            EscalationResult(
                success = true,
                channelType = channelName,
                message = minimisedMessage
            )
        } catch (e: Exception) {
            EscalationResult(
                success = false,
                channelType = channelName,
                message = minimisedMessage,
                error = e.message ?: "Could not launch ACTION_SENDTO intent"
            )
        }
    }
}

