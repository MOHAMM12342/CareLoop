package com.careloop.app.agent

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.careloop.app.agent.worker.FallbackReminderWorker
import com.careloop.app.receiver.AlarmReceiver
import java.util.concurrent.TimeUnit

enum class SchedulingMode {
    EXACT_ALARM,
    WORK_MANAGER_FALLBACK
}

class AlarmCapabilityNegotiator(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun canScheduleExact(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }

    fun scheduleCommitmentReminder(
        commitmentId: String,
        title: String,
        dueAt: Long
    ): SchedulingMode {
        val now = System.currentTimeMillis()
        val delayMs = (dueAt - now).coerceAtLeast(0)

        if (canScheduleExact()) {
            val intent = Intent(context, AlarmReceiver::class.java).apply {
                action = AlarmReceiver.ACTION_TRIGGER_COMMITMENT_ALARM
                putExtra(AlarmReceiver.EXTRA_COMMITMENT_ID, commitmentId)
                putExtra(AlarmReceiver.EXTRA_TITLE, title)
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                commitmentId.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    dueAt,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    dueAt,
                    pendingIntent
                )
            }
            return SchedulingMode.EXACT_ALARM
        } else {
            // Graceful fallback to WorkManager (inexact ±15m window)
            val inputData = Data.Builder()
                .putString(AlarmReceiver.EXTRA_COMMITMENT_ID, commitmentId)
                .putString(AlarmReceiver.EXTRA_TITLE, title)
                .build()

            val workRequest = OneTimeWorkRequestBuilder<FallbackReminderWorker>()
                .setInitialDelay(delayMs, TimeUnit.MILLISECONDS)
                .setInputData(inputData)
                .addTag("commitment_$commitmentId")
                .build()

            WorkManager.getInstance(context).enqueue(workRequest)
            return SchedulingMode.WORK_MANAGER_FALLBACK
        }
    }

    fun cancelCommitmentReminder(commitmentId: String) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = AlarmReceiver.ACTION_TRIGGER_COMMITMENT_ALARM
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            commitmentId.hashCode(),
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
        }
        WorkManager.getInstance(context).cancelAllWorkByTag("commitment_$commitmentId")
    }

    fun createExactAlarmSettingsIntent(): Intent? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                data = Uri.parse("package:${context.packageName}")
            }
        } else {
            null
        }
    }
}

