package com.careloop.app.service

import android.content.Intent
import android.os.Build
import android.service.quicksettings.TileService
import androidx.annotation.RequiresApi
import com.careloop.app.ui.QuickCheckerActivity

@RequiresApi(Build.VERSION_CODES.N)
class QuickCheckerTileService : TileService() {

    override fun onClick() {
        super.onClick()
        val intent = Intent(this, QuickCheckerActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startActivityAndCollapse(intent)
        } else {
            @Suppress("DEPRECATION")
            startActivityAndCollapse(intent)
        }
    }
}

