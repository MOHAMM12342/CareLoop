package com.careloop.app.ui

import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.careloop.app.CareLoopApplication
import com.careloop.app.ui.screens.QuickCheckerScreen
import com.careloop.app.ui.theme.CareLoopTheme

/**
 * Standalone activity designed to pop up directly over the lockscreen / ambient display
 * without requiring login or complex PIN input when the user taps or triggers the shortcut.
 */
class QuickCheckerActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Allow displaying over lockscreen and turning the screen on for immediate elderly access
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as? KeyguardManager
            keyguardManager?.requestDismissKeyguard(this, null)
        } else {
            @Suppress("DEPRECATION")
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            )
        }

        val app = application as CareLoopApplication

        setContent {
            CareLoopTheme {
                QuickCheckerScreen(
                    app = app,
                    onDismiss = { finish() }
                )
            }
        }
    }
}

