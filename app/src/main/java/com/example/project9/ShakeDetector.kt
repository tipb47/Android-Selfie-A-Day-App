package com.example.project9

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.fragment.app.activityViewModels

class ShakeDetector(private val viewModel: AppViewModel) : SensorEventListener {

    private var lastUpdate: Long = 0
    private var lastX = 0f
    private var lastY = 0f
    private var lastZ = 0f

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) { //device shook
            val curTime = System.currentTimeMillis()
            if (curTime - lastUpdate > 100) {
                val diffTime = curTime - lastUpdate
                lastUpdate = curTime

                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                val speed = Math.abs(x + y + z - lastX - lastY - lastZ) / diffTime * 10000


                if (speed > 600) { //if above speed threshold, call viewModel
                    Log.d("selfie", "Shake sensor detected, navigating to selfie fragment")
                    viewModel.onShake()
                }

                lastX = x
                lastY = y
                lastZ = z
            }
        }
    }
}
