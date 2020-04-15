package com.samuelm.quehacerencuarentena

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.database.FirebaseDatabase
import com.samuelm.quehacerencuarentena.MainActivity.Companion.counter
import com.wafflecopter.charcounttextview.CharCountTextView
import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil

class AddPhraseActivity : AppCompatActivity() {

    private lateinit var back_btn: ImageView
    private lateinit var phrase_txt: EditText
    private lateinit var add_btn: Button
    private lateinit var background_layout: ConstraintLayout
    private lateinit var text_counter: CharCountTextView

    private val database = FirebaseDatabase.getInstance()
    private val reference = database.getReference()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_phrase)

        back_btn = findViewById(R.id.back_btn) as ImageView
        phrase_txt = findViewById(R.id.phrase_txt) as EditText
        add_btn = findViewById(R.id.add_btn) as Button
        background_layout = findViewById(R.id.background_layout) as ConstraintLayout
        text_counter = findViewById(R.id.text_counter) as CharCountTextView

        text_counter.setEditText(phrase_txt)
        text_counter.setCharCountChangedListener(object: CharCountTextView.CharCountChangedListener{
            override fun onCountChanged(p0: Int, p1: Boolean) {
                if(p1){
                    add_btn.isEnabled = false
                    add_btn.isClickable = false
                    add_btn.setTextColor(Color.DKGRAY)
                }
                else{
                    add_btn.isEnabled = true
                    add_btn.isClickable = true
                    add_btn.setTextColor(resources.getColor(R.color.textColor))
                }
            }

        })

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
