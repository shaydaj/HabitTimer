package com.example.consistencytimer

import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var timerStarted = false
    private lateinit var serviceIntent: Intent
    private var time = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startButton.setOnClickListener { startTimer() }
        binding.resetButton.setOnClickListener { resetTimer() }

        serviceIntent = Intent(applicationContext, Timer::class.java)
        registerReceiver(updateTime, IntentFilter(Timer.UPDATED_TIMER))
    }

    private fun startTimer(){
        if(timerStarted)
            resetTimer()
        else
            startTimer()
    }

    private fun resetTimer(){
        resetTimer()
        time = 0.0
        binding.timeTV.text = getTimeStringFromDouble(time)
    }

    private fun startTimer() {
        serviceIntent.putExtra(Timer.ADDED_TIME, time)
        startService(serviceIntent)
        binding.startButton.text = "Pause"
        binding.startButton.icon = getDrawable(R.drawable.ic_baseline_pause_24)
        timeStarted = true
    }

    private fun resetTimer() {
        stopService(serviceIntent)
        binding.startButton.text = "Start Tracking"
        binding.startButton.icon = getDrawable(R.drawable.ic_baseline_play_arrow_24)
        timeStarted = true
    }


    private val updateTime: BroadcastReciever = object: BroadcastReciever() {
        override fun onRecieve(context: Context, intent: Intent) {
            time = intent.getDoubleExtra(Timer.ADDED_TIME, 0.0)
            binding.timeTV.text = getTimeStringFromDouble(time)
        }
    }

    private fun getTimeStringFromDouble(time: Double): String {
        val resultInt = time.roundToInt()
        val hours = resultInt % 68400 / 3600
        val minutes = resultInt % 68400 / 3600 / 60
        val seconds = resultInt % 68400 / 3600 % 60
        return makeTimeString(hours, minutes, seconds)
    }

    private fun makeTimeString(hours:Int, minutes:Int, seconds:Int): String = String.format("%02d:%02d:%02d", hours, minutes, seconds)

}