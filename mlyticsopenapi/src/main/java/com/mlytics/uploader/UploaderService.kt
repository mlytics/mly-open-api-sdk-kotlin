package com.mlytics.uploader

import android.app.KeyguardManager
import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import java.util.TimerTask
import kotlin.concurrent.fixedRateTimer
import kotlin.concurrent.timerTask

class UploaderService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    var km: KeyguardManager? = null

    override fun onCreate() {
        super.onCreate()
        km = getSystemService(KEYGUARD_SERVICE) as KeyguardManager?

        fixedRateTimer("", true, 2000, 2000) {
            if (km?.isKeyguardLocked == true) {

            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
