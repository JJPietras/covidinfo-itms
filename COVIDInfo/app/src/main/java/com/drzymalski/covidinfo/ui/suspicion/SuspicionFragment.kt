package com.drzymalski.covidinfo.ui.suspicion

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.drzymalski.covidinfo.R
import com.drzymalski.covidinfo.lib.FragmentBinder
import com.drzymalski.covidinfo.lib.Hospital
import com.drzymalski.covidinfo.lib.SwitchableButton
import com.drzymalski.covidinfo.lib.buttonManagers.CalculateButtonManager
import com.drzymalski.covidinfo.lib.buttonManagers.SwitchButtonManager
import com.drzymalski.covidinfo.lib.buttonManagers.TeleHelpButtonManager
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.DoubleBounce
import kotlinx.android.synthetic.main.fragment_suspicion.*

class SuspicionFragment : Fragment() {

    private lateinit var viewModel: SuspicionViewModel
    private lateinit var progressBar: ProgressBar
    private val switches = mutableListOf<SwitchableButton>()

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
        viewModel = ViewModelProvider(this).get(SuspicionViewModel::class.java)
        return inflater.inflate(R.layout.fragment_suspicion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = R.id.suspicionNearestHospitalLoadingImg
        progressBar = requireView().findViewById(id) as ProgressBar
        val style: Sprite = DoubleBounce()
        progressBar.indeterminateDrawable = style
        progressBar.visibility = View.INVISIBLE
//
        configureSwitchButtons(view)
        configureCalculateButton(view)
        configureTeleHelpButton(view, requireActivity())
        configureUsefulTelephonesButton(view, epidemicButtons[0], "tel:+48 22 25 00 115")
        configureUsefulTelephonesButton(view, epidemicButtons[1], "tel:800190590")
        configureUsefulTelephonesButton(view, epidemicButtons[2], "tel:+48 22 125 66 00")
        configureNearestHospitalButton(view)
        configureBinder(view)
    }

    //TODO: refactor
    @SuppressLint("SetTextI18n")
    fun reactOnChange(closestHospital: Hospital) {

        suspicionNearestHospitalMoveToMap?.setOnClickListener {
            val contact = closestHospital.contact
            val cords = closestHospital.location
            val route = contact.substring(0, contact.indexOf(','))
            val output = StringBuilder("geo:${cords[0]},${cords[1]}?q=")
            val s = ",+"

            output.append(closestHospital.city).append(s).append(closestHospital.name).append(s)
            output.append(closestHospital.title).append(s).append(route).append(s)
            val location = Uri.parse(output.toString())
            val mapIntent = Intent(Intent.ACTION_VIEW, location)
            val intentTitle = "Open with:"
            val chooser = Intent.createChooser(mapIntent, intentTitle)
            startActivity(chooser)
        }
        suspicionNearestHospitalMoveToMap?.isVisible = true

        val displayText = StringBuilder(closestHospital.city).append("\n")
        displayText.append(closestHospital.name).append("\n")
        displayText.append(closestHospital.title).append("\n")
        displayText.append(closestHospital.contact)
        suspicionNearestHospitalDisclaimer?.setTextColor(Color.WHITE)
        suspicionNearestHospitalDisclaimer?.text = displayText.toString()
        suspicionNearestHospitalButtonSphere?.visibility = View.VISIBLE
        progressBar.visibility = View.INVISIBLE
    }

    private fun configureSwitchButtons(view: View) {
        val manager = SwitchButtonManager(view)
        manager.configureSwitchButtons(switches, resources)
    }

    private fun configureCalculateButton(view: View) {
        val manager = CalculateButtonManager(switches)
        manager.configureCalculateButton(view, resources)
    }

    private fun configureTeleHelpButton(view: View, activity: FragmentActivity) {
        val manager = TeleHelpButtonManager()
        manager.configureTeleHelpButton(view, activity)
    }

    private fun configureUsefulTelephonesButton(view: View, buttonId: Int, number: String) {
        val phoneBtn = view.findViewById<Button>(buttonId)//"tel:5551234"
        phoneBtn.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse(number))
            startActivity(callIntent)
        }
    }

    private fun configureNearestHospitalButton(view: View) {
        val locationManager = LocationManager(this)
        val findBtn = suspicionNearestHospitalButtonSphere
        locationManager.configureFindNearestHospitalButton(
            findBtn,
            view,
            requireActivity(),
            progressBar
        )
    }

    private fun configureBinder(view: View) =

        FragmentBinder.bindNavToButton(
                view.findViewById<ImageButton>(R.id.suspicionMenuBtn),
                view,
                R.id.action_nav_suspicion_to_nav_selector
        )
}