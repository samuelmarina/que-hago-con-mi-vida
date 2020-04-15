package com.samuelm.quehacerencuarentena

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {

    lateinit var background_layout: ConstraintLayout
    lateinit var background_animation: AnimationDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        background_layout = findViewById(R.id.background_layout) as ConstraintLayout
        background_animation = background_layout.background as AnimationDrawable
        background_animation.setExitFadeDuration(4500)
        background_animation.start()

    }
}
