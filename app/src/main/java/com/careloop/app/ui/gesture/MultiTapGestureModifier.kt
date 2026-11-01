package com.careloop.app.ui.gesture

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput

/**
 * Multi-tap gesture detector designed specifically for elderly accessibility.
 * When the user taps the screen [requiredTaps] times within [timeWindowMs],
 * it directly triggers [onMultiTap] (e.g. launching the Quick Commitment Checker).
 */
fun Modifier.multiTapDetector(
    requiredTaps: Int = 3,
    timeWindowMs: Long = 1200L,
    enabled: Boolean = true,
    onMultiTap: () -> Unit
): Modifier = if (!enabled) this else pointerInput(requiredTaps, timeWindowMs) {
    var tapCount = 0
    var lastTapTime = 0L

    detectTapGestures(
        onTap = {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastTapTime <= timeWindowMs) {
                tapCount++
            } else {
                tapCount = 1
            }
            lastTapTime = currentTime

            if (tapCount >= requiredTaps) {
                tapCount = 0
                lastTapTime = 0L
                onMultiTap()
            }
        }
    )
}

