package com.drzymalski.covidinfo.ui.vaccine

import android.animation.ValueAnimator
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.drzymalski.covidinfo.R
import com.drzymalski.covidinfo.config.CountryConfig
import com.drzymalski.covidinfo.dataUtils.DateConverter
import com.drzymalski.covidinfo.interfaces.FragmentSettings
import com.drzymalski.covidinfo.lib.FragmentBinder
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.github.aachartmodel.aainfographics.aachartcreator.AAOptions

import com.neovisionaries.i18n.CountryCode
import kotlinx.android.synthetic.main.fragment_today.*
import kotlinx.android.synthetic.main.fragment_vaccine.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.DecimalFormat


class VaccineFragment : Fragment(), FragmentSettings {

    private lateinit var vaccineViewModel: VaccineViewModel
    private var initializer: VaccineInitializer = VaccineInitializer()

    private var compare = false

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        vaccineViewModel =
                ViewModelProvider(this).get(VaccineViewModel::class.java)
        return inflater.inflate(R.layout.fragment_vaccine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        FragmentBinder.bindNavToButton(
                view.findViewById<ImageButton>(R.id.vaccineMenuBtn),
                view,
                R.id.action_vaccineFragment_to_nav_selector
        )
        vaccineSettingsBtn.setOnClickListener{ showSettings() }
        vaccinePickPoland.setOnClickListener{ changeCompare(false) }
        vaccinePickCompare.setOnClickListener{ changeCompare(true) }
        activateLinks()

        refreshData()
    }

    private fun configureChart(chart: AAChartView, options: AAOptions) {
        chart.post(kotlinx.coroutines.Runnable { chart.aa_drawChartWithChartOptions(options) })
    }

    private fun configureCharts(){
        try {
            if (compare){
                configureChart(aaChartViewVaccinations, initializer.configureCompareVaccinesChart())
                configureChart(
                        aaChartViewVaccinationPercentage,
                        initializer.configureCompareVaccinesDailyChart()
                )
            }
            else{
                configureChart(aaChartViewVaccinations, initializer.configureVaccineBarChart())
                configureChart(
                        aaChartViewVaccinationPercentage,
                        initializer.configureNewVaccinationsBarChart()
                )
            }

            animateButtons(20, 50L)
        }catch (ex: Exception){ // No action will be taken
            println(ex)
        }
    }

    private fun changeCompare(value: Boolean){
        compare = value
        if (compare){
            vaccinePickPoland.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#4CAF50"))
            vaccinePickCompare.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#ed135c"))
        }
        else{
            vaccinePickPoland.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#ed135c"))
            vaccinePickCompare.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#4CAF50"))
        }
        GlobalScope.launch {
            configureCharts()
        }
    }


    private fun refreshData(){
        GlobalScope.launch {
            try { // Prevents crashing when data was loaded after changing or refreshing the fragment
                initializer.loadScreenResources()
                configureCharts()
            }catch (ex: Exception){ // No action will be taken
                println(ex)
            }
        }
    }

    private fun activateLinks(){
        vaccineSignUpButton.setOnClickListener {
            val browserIntent =
                Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.gov.pl/web/szczepimysie/jak-sie-szczepic")
                )
            startActivity(browserIntent)
        }
    }

    override fun applySettings(countries: MutableList<CountryConfig>, daysBack: Long) {
        countries.forEach { if (it.code != "" && it.code.length == 2) it.code = CountryCode.getByCode(
                it.code
        ).alpha3 }
        this.initializer.config.config.vaccinationCountriesToCompare = countries
        this.initializer.config.config.daysBackVaccine = daysBack
        this.initializer.config.saveConfig()

        refreshData()
        configureCharts()
    }

    private fun showSettings(){
        if (initializer.csvManager.vaccinationData.count()>0){
            val settingsView = VaccineSettingsView(
                    requireContext(),
                    vaccineLayout,
                    initializer.config.config.vaccinationCountriesToCompare,
                    initializer.config.config.daysBackVaccine,
                    this,
                    initializer.csvManager.vaccinationData
                            .mapNotNull { day -> day.iso_code }.distinct().toMutableList()
            )

            val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
                settingsView.close()
            }
            settingsView.show(callback)
        }
        else{
            Toast.makeText(context, "Poczekaj na załadowanie danych.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showTotals(){
        vaccineCount?.post(kotlinx.coroutines.Runnable {
            vaccineCount.text =
                    DateConverter.coolNumberFormat(initializer.stats.totalVaccinated.toFloat())
        })

        vaccinePercentage?.post(kotlinx.coroutines.Runnable {
            "${DateConverter.coolNumberFormat(initializer.stats.vaccinationPercentage)}%"
                    .also { vaccinePercentage.text = it }
        })
    }


    private fun animateButtons(steps: Int, stepTime: Long) {
        try {
            var animValP = 0f
            val animStepP = initializer.stats.vaccinationPercentage / steps

            var animValT = 0f
            val animStepT = initializer.stats.totalVaccinated / steps

            var step = 0
            while (step < steps) {
                vaccinePercentage?.post(kotlinx.coroutines.Runnable {
                    "${"%.2f".format(animValP)}%"
                            .also { vaccinePercentage.text = it }
                })

                vaccineCount?.post(kotlinx.coroutines.Runnable {
                    vaccineCount.text =
                            DateConverter.coolNumberFormat(animValT)
                })

                animValP += animStepP
                animValT += animStepT
                step += 1
                Thread.sleep(stepTime)
            }

            vaccinePercentage?.post(kotlinx.coroutines.Runnable {
                "${"%.2f".format(initializer.stats.vaccinationPercentage)}%"
                        .also { vaccinePercentage.text = it }
            })

            vaccineCount?.post(kotlinx.coroutines.Runnable {
                vaccineCount.text =
                        DateConverter.coolNumberFormat(initializer.stats.totalVaccinated.toFloat())
            })
        }catch (ex: Exception){ // No action will be taken
            println(ex)
        }
    }
}