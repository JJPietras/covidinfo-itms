package com.drzymalski.covidinfo.ui.illnessSuspicion

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.drzymalski.covidinfo.R
import com.drzymalski.covidinfo.lib.FragmentBinder
import com.drzymalski.covidinfo.lib.SwitchableButton
import com.drzymalski.covidinfo.ui.selector.SelectorFragment
import kotlinx.android.synthetic.main.fragment_suspicion.suspicionSymptomsAgeBtn
import kotlinx.android.synthetic.main.fragment_suspicion.suspicionSymptomsSexBtn
import kotlinx.android.synthetic.main.fragment_suspicion.suspicionSymptomsEthnicsBtn
import kotlinx.android.synthetic.main.fragment_suspicion.suspicionSymptomsDiabetesBtn
import kotlinx.android.synthetic.main.fragment_suspicion.suspicionSymptomsHeartBtn
import kotlinx.android.synthetic.main.fragment_suspicion.suspicionSymptomsLungsBtn
import kotlinx.android.synthetic.main.fragment_suspicion.suspicionSymptomsCancerBtn
import kotlinx.android.synthetic.main.fragment_suspicion.suspicionSymptomsRheumatoidBtn
import kotlinx.android.synthetic.main.fragment_suspicion.suspicionSymptomsImmunityBtn

class IllnessSuspicionFragment : Fragment() {

    private lateinit var viewModel: IllnessSuspicionViewModel
    private val switches = mutableListOf<SwitchableButton>()

    private val arrays = arrayOf(
        R.array.age, R.array.sex, R.array.ethnics, R.array.diabetes, R.array.cardio,
        R.array.lungs, R.array.cancer, R.array.rheumatologic, R.array.immuno
    )

    private val values = arrayOf(
        arrayOf(0, 1, 2, 4, 6), arrayOf(0, 1), arrayOf(0, 2, 1, 1, 1), arrayOf(1, 2, 1, 0),
        arrayOf(1, 2, 0), arrayOf(1, 2, 1, 0), arrayOf(3, 1, 0), arrayOf(2, 0), arrayOf(2, 0)
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this).get(IllnessSuspicionViewModel::class.java)
        return inflater.inflate(R.layout.fragment_suspicion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttons = arrayOf(
            suspicionSymptomsAgeBtn, suspicionSymptomsSexBtn, suspicionSymptomsEthnicsBtn,
            suspicionSymptomsDiabetesBtn, suspicionSymptomsHeartBtn, suspicionSymptomsLungsBtn,
            suspicionSymptomsCancerBtn, suspicionSymptomsRheumatoidBtn, suspicionSymptomsImmunityBtn
        )

        for (i in buttons.indices) {
            val strings = resources.getStringArray(arrays[i])
            switches.add(SwitchableButton(buttons[i], strings, values[i]))
        }

        val text = view.findViewById<TextView>(R.id.suspicionSymptomsResult)
        val calculateBtn = view.findViewById<Button>(R.id.suspicionSymptomsCalculateRiskBtn)
        val riskLevels = resources.getStringArray(R.array.risk)
        calculateBtn.setOnClickListener {
            var sum = 0
            for (switch in switches) sum += switch.getValue()
            Log.wtf("HH", sum.toString())
            when {
                sum < 3 -> text.text = riskLevels[0]
                sum < 6 -> text.text = riskLevels[1]
                else -> text.text = riskLevels[2]
            }
        }

        configureBinder(view)
    }

    private fun configureBinder(view: View) =
        FragmentBinder.bindToButton(
            view.findViewById(R.id.suspicionMenuBtn),
            SelectorFragment(),
            requireActivity()
        )
}