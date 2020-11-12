package com.example.zad1

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnSecondActivity -> startActivity(Intent(this, SecondActivity::class.java))
            R.id.btnGoogle -> {
                val browserIntent = Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/watch?v=oHg5SJYRHA0"))
                val chooser = Intent.createChooser(browserIntent, R.string.chooser_title.toString())
                startActivity(chooser)
            }
            R.id.btnCompute -> startActivity(Intent(this, ComputeActivity::class.java))
            R.id.btnCMap -> {
                val gmmIntentUri = Uri.parse("google.streetview:cbll=51.747394,19.453807")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }
            R.id.btnPhone -> {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:666"))
                startActivity(intent)
            }
        }
    }

}