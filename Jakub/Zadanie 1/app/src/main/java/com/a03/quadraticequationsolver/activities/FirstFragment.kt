package com.a03.quadraticequationsolver.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.a03.quadraticequationsolver.R
import com.a03.quadraticequationsolver.logic.Calculator
import kotlinx.android.synthetic.main.fragment_first.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private val calculator = Calculator()

    private lateinit var calculateButton: Button

    private lateinit var aField: EditText
    private lateinit var bField: EditText
    private lateinit var cField: EditText

    private lateinit var resultField: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        calculateButton = view.findViewById(R.id.CalculateButton)
        calculateButton.setOnClickListener { calculateRoots() }

        aField = view.findViewById(R.id.Num1EditText)
        bField = view.findViewById(R.id.Num2EditText)
        cField = view.findViewById(R.id.Num3EditText)

        resultField = view.findViewById(R.id.textview_first)
    }

    private fun calculateRoots() {
        substituteEmptyEditTexts()
        val results = calculator.calculateRoots(
            aField.text.toString(),
            bField.text.toString(),
            cField.text.toString()
        )

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