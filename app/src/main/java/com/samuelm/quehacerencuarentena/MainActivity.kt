package com.samuelm.quehacerencuarentena

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Vibrator
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import java.util.*

class MainActivity : AppCompatActivity(), SensorEventListener {
    val FADE_DURATION: Int = 1500
    val START_OFFSET: Int = 1000
    val VIBRATE_TIME: Long = 250
    val THRESHOLD: Int = 240
    val SHAKE_COUNT: Int = 2
    private var RANDOM: Random = Random()
    private lateinit var vibrator: Vibrator
    private lateinit var sensorManager: SensorManager
    private lateinit var sensor: Sensor
    private var lastX: Float = 0.0f
    private var lastY: Float = 0.0f
    private var lastZ: Float = 0.0f
    private var shakeCount: Int = 0

    private lateinit var frase_txt: TextView
    private lateinit var magic_ball_img: ImageView

    private lateinit var ball_animation: Animation

    lateinit var background_layout: ConstraintLayout
    lateinit var background_animation: AnimationDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        background_layout = findViewById(R.id.background_layout) as ConstraintLayout
        background_animation = background_layout.background as AnimationDrawable
        background_animation.setExitFadeDuration(4500)
        background_animation.start()

        frase_txt = findViewById(R.id.frase_txt) as TextView
        magic_ball_img = findViewById(R.id.magic_ball_img) as ImageView

        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        ball_animation = AnimationUtils.loadAnimation(this, R.anim.shake)


    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
        showAnswer("Agita pa ver que hacer con tu vida", false)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(event?.sensor?.type == Sensor.TYPE_ACCELEROMETER){
            if(isShakeEnough(event?.values[0], event?.values[1], event?.values[2])){
                showAnswer(getAnswer(), false)
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

        if(gForce > (this.THRESHOLD / 100f)){
            magic_ball_img.startAnimation(ball_animation)
            shakeCount += 1

            if(shakeCount > SHAKE_COUNT){
                shakeCount = 0
                lastX = 0f
                lastY = 0f
                lastZ = 0f
                return true
            }
        }
        return false
    }

    private fun showAnswer(answer: String, withAnim: Boolean){
        if(withAnim){
            magic_ball_img.startAnimation(ball_animation)
        }

        frase_txt.visibility = View.INVISIBLE
        frase_txt.setText(answer)
        val animation: AlphaAnimation = AlphaAnimation(0f, 1f)
        animation.startOffset = START_OFFSET.toLong()
        frase_txt.visibility = View.VISIBLE
        animation.duration = FADE_DURATION.toLong()
        frase_txt.startAnimation(animation)

        vibrator.vibrate(VIBRATE_TIME)
    }

    private fun getAnswer(): String{
        return "Absolutamente nada"
    }
}
