package com.a03.zadanie3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window.FEATURE_NO_TITLE
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN

class PaintActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(FLAG_FULLSCREEN,FLAG_FULLSCREEN)
        requestWindowFeature(FEATURE_NO_TITLE)

        val drawView = DrawView(this)
        setContentView(drawView)
        drawView.requestFocus()


    }
}