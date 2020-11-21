package com.drzymalski.covidinfo.ui.illnessSuspicion

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.SensorEventListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.drzymalski.covidinfo.R
import com.drzymalski.covidinfo.lib.FragmentBinder
import com.drzymalski.covidinfo.lib.SwitchableButton
import com.drzymalski.covidinfo.ui.selector.SelectorFragment
import com.google.android.gms.location.LocationServices
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

    private val epidemicButtons = arrayOf(
        R.id.telephonesSanitaryEpidemicTelephoneButton,
        R.id.usefulTelephonesNFZBtn,
        R.id.usefulTelephonesInfoBtn
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
        configureSwitchButtons()
        configureCalculateButton(view)
        configureTeleHelpButton(view)
        configureUsefulTelephonesButton(view, epidemicButtons[0], "tel:+48 22 25 00 115")
        configureUsefulTelephonesButton(view, epidemicButtons[1], "tel:800190590")
        configureUsefulTelephonesButton(view, epidemicButtons[2], "tel:+48 22 125 66 00")

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        if (ActivityCompat.checkSelfPermission(
                view.context, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                view.context, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude

            }
        }

        configureBinder(view)
    }

    private fun configureSwitchButtons() {
        val buttons = arrayOf(
            suspicionSymptomsAgeBtn, suspicionSymptomsSexBtn, suspicionSymptomsEthnicsBtn,
            suspicionSymptomsDiabetesBtn, suspicionSymptomsHeartBtn, suspicionSymptomsLungsBtn,
            suspicionSymptomsCancerBtn, suspicionSymptomsRheumatoidBtn, suspicionSymptomsImmunityBtn
        )

        for (i in buttons.indices) {
            val strings = resources.getStringArray(arrays[i])
            switches.add(SwitchableButton(buttons[i], strings, values[i]))
        }
    }

    private fun configureCalculateButton(view: View) {
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
    }

    private fun configureTeleHelpButton(view: View) {
        val teleHelpBtn = view.findViewById<Button>(R.id.suspicionTeleHelpBtn)
        teleHelpBtn.setOnClickListener {
            val webAddress = Uri.parse("https://pacjent.gov.pl/ewizyta")
            val gotoGoogle = Intent(Intent.ACTION_VIEW, webAddress)
            val chooser = Intent.createChooser(gotoGoogle, "Open with:")
            startActivity(chooser)
        }
    }

    private fun configureUsefulTelephonesButton(view: View, buttonId: Int, number: String) {
        val phoneBtn = view.findViewById<Button>(buttonId)//"tel:5551234"
        phoneBtn.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse(number))
            startActivity(callIntent)
        }
    }

    private fun configureBinder(view: View) =
        FragmentBinder.bindToButton(
            view.findViewById(R.id.suspicionMenuBtn),
            SelectorFragment(),
            requireActivity()
        )
}