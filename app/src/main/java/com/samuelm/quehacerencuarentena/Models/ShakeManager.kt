package com.samuelm.quehacerencuarentena.Models

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Vibrator
import com.samuelm.quehacerencuarentena.Constants
import com.samuelm.quehacerencuarentena.Controllers.MainActivity

class ShakeManager: SensorEventListener {

    private lateinit var vibrator: Vibrator
    private lateinit var sensorManager: SensorManager
    lateinit var sensor: Sensor
    private var lastX: Float = 0.0f
    private var lastY: Float = 0.0f
    private var lastZ: Float = 0.0f
    private var shakeCount: Int = 0

    companion object {
        var shared: ShakeManager = ShakeManager()
    }

    fun initialize(sensorManager: SensorManager, vibrator: Vibrator){
        this.vibrator = vibrator
        this.sensorManager = sensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(event?.sensor?.type == Sensor.TYPE_ACCELEROMETER){
            if(isShakeEnough(event?.values[0], event?.values[1], event?.values[2])){
                MainActivity.db.getRandomAnswer()
            }
        }
    }

    private fun isShakeEnough(x: Float, y: Float, z: Float): Boolean {
        val gravity: Float = SensorManager.GRAVITY_EARTH
        val gX = x / gravity
        val gY = y / gravity
        val gZ = z / gravity

        val gForce: Float = Math.sqrt((gX*gX + gY*gY + gZ*gZ).toDouble()).toFloat()

        lastX = x
        lastY = y
        lastZ = z

        if(gForce > (Constants.THRESHOLD / 100f)){
            shakeCount += 1

            if(shakeCount > Constants.SHAKE_COUNT){
                shakeCount = 0
                lastX = 0f
                lastY = 0f
                lastZ = 0f
                return true
            }
        }
        return false
    }
}