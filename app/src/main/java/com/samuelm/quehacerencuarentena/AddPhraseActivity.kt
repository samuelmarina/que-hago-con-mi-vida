package com.samuelm.quehacerencuarentena

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.database.FirebaseDatabase
import com.samuelm.quehacerencuarentena.MainActivity.Companion.counter
import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil

class AddPhraseActivity : AppCompatActivity() {

    private lateinit var back_btn: ImageView
    private lateinit var phrase_txt: EditText
    private lateinit var add_btn: Button
    private lateinit var background_layout: ConstraintLayout

    private val database = FirebaseDatabase.getInstance()
    private val reference = database.getReference()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_phrase)

        back_btn = findViewById(R.id.back_btn) as ImageView
        phrase_txt = findViewById(R.id.phrase_txt) as EditText
        add_btn = findViewById(R.id.add_btn) as Button
        background_layout = findViewById(R.id.background_layout) as ConstraintLayout

        back_btn.setOnClickListener {
            finish()
        }

        add_btn.setOnClickListener {
            if(phrase_txt.text.isEmpty()){
                Toast.makeText(applicationContext, "Escribe algo ps", Toast.LENGTH_SHORT).show()
            }
            else{
                counter += 1
                val path = "Frase$counter"
                reference.child(path).setValue(phrase_txt.text.toString())
                reference.child("Counter").setValue(counter)
                finish()
            }
        }

        background_layout.setOnClickListener {
            UIUtil.hideKeyboard(this)
        }

    }
}
