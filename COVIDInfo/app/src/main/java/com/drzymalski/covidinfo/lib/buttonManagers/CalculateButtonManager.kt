package com.drzymalski.covidinfo.lib.buttonManagers

import android.content.res.Resources
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.drzymalski.covidinfo.R
import com.drzymalski.covidinfo.lib.SwitchableButton

class CalculateButtonManager(private val switches: MutableList<SwitchableButton>) {

    private lateinit var text: TextView
    private lateinit var calculateBtn: Button
    private lateinit var riskLevels: Array<out String>

    fun configureCalculateButton(view: View, resources: Resources) {
        text = view.findViewById(R.id.suspicionSymptomsResult)
        calculateBtn = view.findViewById(R.id.suspicionSymptomsCalculateRiskBtn)
        riskLevels = resources.getStringArray(R.array.risk)
        makeCalculateButtonListener(calculateBtn)
    }

    private fun makeCalculateButtonListener(button: Button) {
        button.setOnClickListener {
            var sum = 0
            for (switch in switches) sum += switch.getValue()
            when {
                sum < 3 -> text.text = riskLevels[0]
                sum < 6 -> text.text = riskLevels[1]
                else -> text.text = riskLevels[2]
            }
        }
    }
}