package com.example.consistencytimer

import android.app.Service
import android.content.Intent
import android.os.IBinder
import java.util.*

class Timer:Service()

{
    override fun onBind(p0: Intent): IBinder? = null

    private val timer = Timer()

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int
    {
        val time = intent.getDoubleExtra(ADDED_TIME, 0.0)
        timer.scheduleAtFixedRate(TimeTask(time), 0, 1000)
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        timer.cancel()
        super.onDestroy()
    }

    private inner class TimeTask(private var time: Double): TimerTask() {
        override fun run() {
            val intent = Intent(UPDATED_TIMER)
            time++
            intent.putExtra(ADDED_TIME, time)
            sendBroadcast(intent)
        }
    }

    companion object {
        const val UPDATED_TIMER = "updatedTimer"
        const val ADDED_TIME = "addedTime"
    }
}