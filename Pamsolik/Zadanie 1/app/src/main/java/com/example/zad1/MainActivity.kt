package com.example.zad1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.zad1.R.string
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnCompute.setOnClickListener { compute() }
    }

    private fun compute() = try {
        val a: Float = txtA.text.toString().toFloat()
        val b: Float = txtB.text.toString().toFloat()
        val c: Float = txtC.text.toString().toFloat()
        val delta: Float = b*b - 4*a*c
        var outputText = ""
        when {
            delta==0f -> {
                val x1:Float = (-b)/(2*a)
                outputText = "This equation has 1 root $x1 , the square discriminant equals $delta"
            }
            delta>0f -> {
                val x1:Float = (-b + sqrt(delta))/(2*a)
                val x2:Float = (-b - sqrt(delta))/(2*a)
                outputText = "This equation has 2 roots: $x1 and $x2, the square discriminant equals $delta"
            }
            delta<0f -> outputText = "This equation has no roots, the square discriminant equals $delta"
        }
        txt_output.text = outputText
    }catch (ex: NumberFormatException) {
        txt_output.text = getString(string.error)
    }
}