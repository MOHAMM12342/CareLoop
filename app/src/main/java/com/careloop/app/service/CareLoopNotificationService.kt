package com.careloop.app.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.careloop.app.receiver.AlarmReceiver
import com.careloop.app.ui.MainActivity

class CareLoopNotificationService(private val context: Context) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()

            val reminderChannel = NotificationChannel(
                CHANNEL_REMINDERS,
                "CareLoop Medication & Health Commitments",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "High-priority alarms for daily commitments"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 200, 500)
                setSound(soundUri, audioAttributes)
            }

            val escalationChannel = NotificationChannel(
                CHANNEL_ESCALATIONS,
                "CareLoop Caregiver Escalations",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Alerts regarding family notification escalations"
                enableVibration(true)
            }

            notificationManager.createNotificationChannel(reminderChannel)
            notificationManager.createNotificationChannel(escalationChannel)
        }
    }

    fun showCommitmentNotification(commitmentId: String, title: String, isReNotification: Boolean = false) {
        val tapIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("commitmentId", commitmentId)
        }
        val tapPendingIntent = PendingIntent.getActivity(
            context,
            commitmentId.hashCode(),
            tapIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val takenIntent = Intent(context, AlarmReceiver::class.java).apply {
            action = AlarmReceiver.ACTION_COMMITMENT_TAKEN
            putExtra(AlarmReceiver.EXTRA_COMMITMENT_ID, commitmentId)
        }
        val takenPendingIntent = PendingIntent.getBroadcast(
            context,
            (commitmentId + "_taken").hashCode(),
            takenIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val snoozeIntent = Intent(context, AlarmReceiver::class.java).apply {
            action = AlarmReceiver.ACTION_COMMITMENT_SNOOZE
            putExtra(AlarmReceiver.EXTRA_COMMITMENT_ID, commitmentId)
        }
        val snoozePendingIntent = PendingIntent.getBroadcast(
            context,
            (commitmentId + "_snooze").hashCode(),
            snoozeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationTitle = if (isReNotification) "⚠️ Attention: $title" else "Time for: $title"
        val notificationBody = if (isReNotification) {
            "Please confirm you have completed this commitment. If unconfirmed, family will be notified."
        } else {
            "Tap 'Done' to record, or 'Snooze' for 15 minutes."
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_REMINDERS)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle(notificationTitle)
            .setContentText(notificationBody)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(true)
            .setContentIntent(tapPendingIntent)
            .addAction(android.R.drawable.checkbox_on_background, "Done ✓", takenPendingIntent)
            .addAction(android.R.drawable.ic_popup_sync, "Snooze 15m", snoozePendingIntent)

        notificationManager.notify(commitmentId.hashCode(), builder.build())
    }

    fun dismissNotification(commitmentId: String) {
        notificationManager.cancel(commitmentId.hashCode())
    }

    fun showEscalationStatusNotification(title: String, message: String) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ESCALATIONS)
            .setSmallIcon(android.R.drawable.stat_notify_error)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }

    companion object {
        const val CHANNEL_REMINDERS = "careloop_reminders_channel"
        const val CHANNEL_ESCALATIONS = "careloop_escalations_channel"
    }
}

