package com.a03.zadanie3

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val secondActivityBtn = findViewById<Button>(R.id.secondActivityBtn)
        secondActivityBtn.setOnClickListener {
            val startIntent = Intent(applicationContext, SecondActivity::class.java)
            startActivity(startIntent)
        }

        val googleBtn = findViewById<Button>(R.id.googleBtn)
        googleBtn.setOnClickListener {
            val google = "http://www.google.com"
            val webAddress = Uri.parse(google)
            val gotoGoogle = Intent(Intent.ACTION_VIEW, webAddress)
            val intentTitle = "Open with:"
            val chooser = Intent.createChooser(gotoGoogle, intentTitle)
            //if (gotoGoogle.resolveActivity(packageManager) != null) {
            startActivity(chooser)
            //}
        }

        val mapBtn = findViewById<Button>(R.id.mapBtn)
        mapBtn.setOnClickListener {
            val location = Uri.parse("geo:0,0?q=1600+Amphitheatre+Parkway,+Mountain+View,+Cali")
            val mapIntent = Intent(Intent.ACTION_VIEW, location)
            val intentTitle = "Open with:"
            val chooser = Intent.createChooser(mapIntent, intentTitle)
            //if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(chooser)
            //}
        }

        val phoneBtn = findViewById<Button>(R.id.phoneBtn)
        phoneBtn.setOnClickListener {
            val number = Uri.parse("tel:5551234")
            val callIntent = Intent(Intent.ACTION_DIAL, number)
            //if (callIntent.resolveActivity(packageManager) != null) {
            startActivity(callIntent)
            //}
        }

        val deltaBtn = findViewById<Button>(R.id.deltaBtn)
        deltaBtn.setOnClickListener {
            val solverIntent = Intent(applicationContext, DeltaActivity::class.java)
            startActivity(solverIntent)
        }

        val paintBtn = findViewById<Button>(R.id.paintBtn)
        paintBtn.setOnClickListener {
            val paintIntent = Intent(applicationContext, PaintActivity::class.java)
            startActivity(paintIntent)
        }
    }
}