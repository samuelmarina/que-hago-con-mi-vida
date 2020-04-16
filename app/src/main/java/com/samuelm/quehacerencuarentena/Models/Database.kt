package com.samuelm.quehacerencuarentena.Models

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.samuelm.quehacerencuarentena.Controllers.MainActivity

interface DatabaseDelegate {
    fun didFailWithError(e: DatabaseError)
    fun didGetAnswer(answer: String)
}

class Database {
    val database = FirebaseDatabase.getInstance()
    val reference = database.getReference()
    var delegate: DatabaseDelegate? = null

    fun initialize(){
        reference.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                delegate?.didFailWithError(p0)

            }

            override fun onDataChange(p0: DataSnapshot) {
                var a = p0.child("Counter").value as Long
                MainActivity.counter = a.toInt()
            }
        })
    }

    fun getRandomAnswer(){
        val random = (0..MainActivity.counter).random()
        val path = "Frase$random"
        var answer = ""

        reference.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                answer = "Agita mas duro, jevita"
                delegate?.didFailWithError(p0)
            }

            override fun onDataChange(p0: DataSnapshot) {
                answer = p0.child(path).value as String
                delegate?.didGetAnswer(answer)
            }

        })
    }
}