package com.drzymalski.covidinfo.lib.buttonManagers

import android.content.res.Resources
import android.view.View
import android.widget.Button
import com.drzymalski.covidinfo.R
import com.drzymalski.covidinfo.data.Diseases
import com.drzymalski.covidinfo.lib.SwitchableButton

class SwitchButtonManager(view: View) {

    private var buttons: Array<Button> = arrayOf(
        view.findViewById(R.id.suspicionSymptomsAgeBtn),
        view.findViewById(R.id.suspicionSymptomsSexBtn),
        view.findViewById(R.id.suspicionSymptomsEthnicsBtn),
        view.findViewById(R.id.suspicionSymptomsDiabetesBtn),
        view.findViewById(R.id.suspicionSymptomsHeartBtn),
        view.findViewById(R.id.suspicionSymptomsLungsBtn),
        view.findViewById(R.id.suspicionSymptomsCancerBtn),
        view.findViewById(R.id.suspicionSymptomsRheumatoidBtn),
        view.findViewById(R.id.suspicionSymptomsImmunityBtn)
    )

    fun configureSwitchButtons(switches: MutableList<SwitchableButton>, resources: Resources) {
        for (i in buttons.indices) {
            val strings = resources.getStringArray(Diseases.names[i])
            switches.add(SwitchableButton(buttons[i], strings, Diseases.severities[i]))
        }
    }
}