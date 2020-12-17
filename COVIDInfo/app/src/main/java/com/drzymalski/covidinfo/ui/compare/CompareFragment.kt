package com.drzymalski.covidinfo.ui.compare

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.drzymalski.covidinfo.R
import com.drzymalski.covidinfo.config.CountryConfig
import com.drzymalski.covidinfo.lib.FragmentBinder
import com.drzymalski.covidinfo.interfaces.FragmentSettings
import com.drzymalski.covidinfo.ui.selector.SelectorFragment
import com.drzymalski.covidinfo.ui.settings.SettingsView
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAOptions
import kotlinx.android.synthetic.main.fragment_compare.aaChartViewActiveCases
import kotlinx.android.synthetic.main.fragment_compare.aaChartViewDied
import kotlinx.android.synthetic.main.fragment_compare.aaChartViewNewCases
import kotlinx.android.synthetic.main.fragment_compare.aaChartViewRecovered
import kotlinx.android.synthetic.main.fragment_compare.aaChartViewTotalCases
import kotlinx.android.synthetic.main.fragment_compare.statisticsSettingsBtn
import kotlinx.android.synthetic.main.fragment_compare.root_layout
import kotlinx.android.synthetic.main.fragment_compare.statisticsReload
import kotlinx.android.synthetic.main.fragment_compare.daysPicker
import kotlinx.android.synthetic.main.fragment_compare.todaySource1
import kotlinx.coroutines.*

class CompareFragment : Fragment(), FragmentSettings {

    private lateinit var viewModel: CompareViewModel
    private var initializer: CompareInitializer = CompareInitializer()

    private var daysChanged = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(CompareViewModel::class.java)
        return inflater.inflate(R.layout.fragment_compare, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadDataAndRefresh()

        FragmentBinder.bindToButton(
            view.findViewById(R.id.statisticsMenuBtn),
            SelectorFragment(),
            requireActivity()
        )

        statisticsSettingsBtn.setOnClickListener{
            showSettings()
        }

        statisticsReload.setOnClickListener {
            initializer.config.config.daysBackCompare = daysPicker.value.toLong()
            initializer.config.saveConfig()
            loadDataAndRefresh()
            daysChanged = false
        }

        daysPicker.minValue = 7
        daysPicker.maxValue = 365
        daysPicker.value = initializer.config.config.daysBackCompare.toInt()
        daysPicker.setOnValueChangedListener { _, _, _ -> daysChanged = true }
        activateLinks()
    }

    private fun configureChart(chart: AAChartView, options: AAOptions) {
        chart.post(Runnable {
            chart.clearCache(true)
            chart.clearFormData()
            chart.aa_drawChartWithChartOptions(options)
        })
    }

    private fun configureCharts(){
        try{
            configureChart(aaChartViewNewCases, initializer.configureNewCasesBarChart())
            configureChart(aaChartViewTotalCases, initializer.configureTotalCasesBarChart())
            configureChart(aaChartViewActiveCases, initializer.configureActiveBarChart())
            configureChart(aaChartViewRecovered, initializer.configureRecoveredBarChart())
            configureChart(aaChartViewDied, initializer.configureDeathsBarChart())
        }catch (ex: Exception){ // No action will be taken
            println(ex)
        }
    }

    private fun loadDataAndRefresh(){
        GlobalScope.launch {
            try { // Prevents crashing when data was loaded after changing or refreshing the fragment
                initializer.loadScreenResources()
                configureCharts()
                initializer.stats.clear()
            }catch (ex: Exception){ // No action will be taken
                println(ex)
            }
        }
    }

    private fun showSettings(){
        val settingsView = SettingsView(requireContext(), root_layout, initializer.config.config.countriesToCompare, initializer.config.config.daysBackCompare, this)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            settingsView.close()
        }
        settingsView.show(callback)
    }

    override fun applySettings(countries: MutableList<CountryConfig>, daysBack: Long){
        this.initializer.config.config.countriesToCompare = countries
        this.initializer.config.config.daysBackCompare = daysBack
        this.initializer.config.saveConfig()
        daysPicker.value = daysBack.toInt()

        loadDataAndRefresh()
    }
    private fun activateLinks(){
        todaySource1.setOnClickListener {
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://documenter.getpostman.com/view/2220438/SzYevv9u?version=latest"))
            startActivity(browserIntent)
        }
    }
}