package com.a03.zadanie3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.a03.zadanie3.logic.Calculator

class DeltaActivity : AppCompatActivity() {

    private val calculator = Calculator()

    private lateinit var calculateButton: Button

    private lateinit var aField: EditText
    private lateinit var bField: EditText
    private lateinit var cField: EditText

    private lateinit var resultField: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delta)

        calculateButton = findViewById(R.id.CalculateButton)
        calculateButton.setOnClickListener { calculateRoots() }

        aField = findViewById(R.id.Num1EditText)
        bField = findViewById(R.id.Num2EditText)
        cField = findViewById(R.id.Num3EditText)

        resultField = findViewById(R.id.textview_first)
    }

    private fun calculateRoots() {
        substituteEmptyEditTexts()

        var results: Array<Float>? = null
        try {
            results = calculator.calculateRoots(
                aField.text.toString(),
                bField.text.toString(),
                cField.text.toString()
            )
        } catch (exception: NumberFormatException) {
            resultField.text = "Wrong number format!"
            return
        }

        setResultTextField(results)
    }

    private fun setResultTextField(results: Array<Float>?) {
        resultField.text = when {
            results == null -> "a is equal 0. Provided equation is not quadratic."
            results[2] < 0f -> "Discriminant (${results[2]}) is < 0. Equation has no real roots."
            results[0] == results[1] -> "Equation has double root: x1 = x2 = ${results[1]}. " +
                    "Discriminant equals ${results[2]}."
            else -> "Equation has two real roots x1 = ${results[0]} and x2 = ${results[1]}." +
                    "Discriminant equals ${results[2]}."
        }
    }

    private fun substituteEmptyEditTexts() {
        if (aField.text.toString() == "") aField.setText("0")
        if (bField.text.toString() == "") bField.setText("0")
        if (cField.text.toString() == "") cField.setText("0")
    }
}