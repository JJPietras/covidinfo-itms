package com.drzymalski.covidinfo.ui.vaccine

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
import com.drzymalski.covidinfo.interfaces.FragmentSettings
import com.drzymalski.covidinfo.lib.FragmentBinder
import com.drzymalski.covidinfo.ui.selector.SelectorFragment
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAOptions
import com.neovisionaries.i18n.CountryCode
import kotlinx.android.synthetic.main.fragment_vaccine.vaccineSettingsBtn
import kotlinx.android.synthetic.main.fragment_vaccine.aaChartViewVaccinations
import kotlinx.android.synthetic.main.fragment_vaccine.aaChartViewVaccinationPercentage
import kotlinx.android.synthetic.main.fragment_vaccine.vaccineSignUpButton
import kotlinx.android.synthetic.main.fragment_vaccine.vaccineLayout
import kotlinx.android.synthetic.main.fragment_vaccine.vaccinePickCompare
import kotlinx.android.synthetic.main.fragment_vaccine.vaccinePickPoland
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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

    @ExperimentalStdlibApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        FragmentBinder.bindToButton(
                view.findViewById<ImageButton>(R.id.vaccineMenuBtn),
                SelectorFragment(),
                requireActivity()
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
                configureChart(aaChartViewVaccinationPercentage, initializer.configureCompareVaccinesDailyChart())
            }
            else{
                configureChart(aaChartViewVaccinations, initializer.configureVaccineBarChart())
                configureChart(aaChartViewVaccinationPercentage, initializer.configureNewVaccinationsBarChart())
            }
        }catch (ex: Exception){ // No action will be taken
            println(ex)
        }
    }

    private fun changeCompare(value: Boolean){
        compare = value
        configureCharts()
        if (compare){
            vaccinePickPoland.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#4CAF50"))
            vaccinePickCompare.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#ed135c"))
        }
        else{
            vaccinePickPoland.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#ed135c"))
            vaccinePickCompare.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#4CAF50"))
        }
    }

    @ExperimentalStdlibApi
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
                Intent(Intent.ACTION_VIEW, Uri.parse("https://www.gov.pl/web/szczepimysie/jak-sie-szczepic"))
            startActivity(browserIntent)
        }
    }

    @ExperimentalStdlibApi
    override fun applySettings(countries: MutableList<CountryConfig>, daysBack: Long) {
        countries.forEach { if (it.code != "" && it.code.length == 2) it.code = CountryCode.getByCode(it.code).alpha3 }
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
                            .mapNotNull {  day -> day.iso_code}.distinct().toMutableList()
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
}