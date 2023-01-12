package com.example.assignment1

import android.app.Activity
import android.os.Bundle

class InfoActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id = intent.getIntExtra("id",0)
        renderInfo(id)
    }
    fun renderInfo(id : Int) {
        when(id) {
            R.id.mengrong -> setContentView(R.layout.activity_mengrong)
            R.id.wesley -> setContentView(R.layout.activity_wesley)
            R.id.jon -> setContentView(R.layout.activity_jon)
            R.id.keefe -> setContentView(R.layout.activity_keefe)
            R.id.minyao -> setContentView(R.layout.activity_minyao)
            R.id.lynette -> setContentView(R.layout.activity_lynette)
        }
    }
}