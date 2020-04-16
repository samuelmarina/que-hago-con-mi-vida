package com.samuelm.quehacerencuarentena.Controllers

import android.content.Context
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.hardware.SensorManager
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Vibrator
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseError
import com.samuelm.quehacerencuarentena.Constants
import com.samuelm.quehacerencuarentena.Models.Database
import com.samuelm.quehacerencuarentena.Models.DatabaseDelegate
import com.samuelm.quehacerencuarentena.Models.ShakeManager
import com.samuelm.quehacerencuarentena.R

// SensorEventListener,
class MainActivity : AppCompatActivity(), DatabaseDelegate {

    private lateinit var vibrator: Vibrator
    private lateinit var sensorManager: SensorManager

    private lateinit var frase_txt: TextView
    private lateinit var magic_ball_img: ImageView
    private lateinit var add_phrase_btn: Button

    private lateinit var ball_animation: Animation

    lateinit var background_layout: LinearLayout
    lateinit var background_animation: AnimationDrawable

    companion object{
        var counter: Int = 0
        var db: Database = Database()
    }

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var audioManager: AudioManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db.delegate = this
        db.initialize()

        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager


        mediaPlayer = MediaPlayer.create(this, R.raw.bruh)
//        mediaPlayer.isLooping = true
//        mediaPlayer.start()

        background_layout = findViewById(R.id.background_layout) as LinearLayout
        background_animation = background_layout.background as AnimationDrawable
        background_animation.setExitFadeDuration(4500)
        background_animation.start()

        frase_txt = findViewById(R.id.frase_txt) as TextView
        magic_ball_img = findViewById(R.id.magic_ball_img) as ImageView
        add_phrase_btn = findViewById(R.id.add_phrase_btn) as Button

        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        ShakeManager.shared.initialize(sensorManager, vibrator)
//        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        ball_animation = AnimationUtils.loadAnimation(this,
            R.anim.shake
        )



        add_phrase_btn.setOnClickListener {
            val intent = Intent(this, AddPhraseActivity::class.java)
            startActivity(intent)
        }

    }

    override fun didFailWithError(e: DatabaseError) {
        showAnswer("Error de conexión", false)
        println(e.details)
    }

    override fun didGetAnswer(answer: String) {
        magic_ball_img.startAnimation(ball_animation)
        showAnswer(answer, false)
    }



    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(ShakeManager.shared, ShakeManager.shared.sensor, SensorManager.SENSOR_DELAY_UI)
        showAnswer("Agita pa ver que hacer con tu vida", false)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(ShakeManager.shared)
    }

    private fun showAnswer(answer: String, withAnim: Boolean){
        if(withAnim){
            magic_ball_img.startAnimation(ball_animation)
        }

        frase_txt.visibility = View.INVISIBLE
        frase_txt.setText(answer)

        val animation: AlphaAnimation = AlphaAnimation(0f, 1f)
        animation.startOffset = Constants.START_OFFSET.toLong()

        frase_txt.visibility = View.VISIBLE
        animation.duration = Constants.FADE_DURATION.toLong()

        frase_txt.startAnimation(animation)

        vibrator.vibrate(Constants.VIBRATE_TIME)
    }
}
