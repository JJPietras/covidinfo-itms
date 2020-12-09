package com.drzymalski.covidinfo.ui.compare

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.drzymalski.covidinfo.R
import com.drzymalski.covidinfo.apiUtils.ApiManager
import com.drzymalski.covidinfo.config.CountryConfig
import com.drzymalski.covidinfo.lib.FragmentBinder
import com.drzymalski.covidinfo.dataUtils.CompareCasesStats
import com.drzymalski.covidinfo.dataUtils.DateConverter
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
import kotlinx.coroutines.*
import kotlin.math.abs

class CompareFragment : Fragment(), FragmentSettings {

    private lateinit var viewModel: CompareViewModel
    private var initializer: CompareInitializer = CompareInitializer()
    private var dataSize = 0
    private var daysChanged = false
    private var jobs =  mutableListOf<Job>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this).get(CompareViewModel::class.java)
        return inflater.inflate(R.layout.fragment_compare, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val job = GlobalScope.launch {
            initializer.loadScreenResources()
            loadComparison()
        }
        jobs.plusAssign(job)

        FragmentBinder.bindToButton(
            view.findViewById(R.id.statisticsMenuBtn),
            SelectorFragment(),
            requireActivity()
        )

        statisticsSettingsBtn.setOnClickListener{
            val settingsView = SettingsView(requireContext(), root_layout, initializer.config.config.countriesToCompare, initializer.config.config.daysBackCompare, this)
            val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
                settingsView.close()
            }
            settingsView.show(callback)
        }

        statisticsReload.setOnClickListener {
            initializer.config.config.daysBackCompare = daysPicker.value.toLong()
            initializer.config.saveConfig()
            loadComparison()
            daysChanged = false
        }
        daysPicker.minValue = 7
        daysPicker.maxValue = 365
        daysPicker.value = initializer.config.config.daysBackCompare.toInt()
        daysPicker.setOnValueChangedListener { _, _, _ ->
            daysChanged = true
        }
    }

    private fun configureChart(chart: AAChartView, options: AAOptions) {
        chart.post(Runnable {
            chart.clearCache(true)
            chart.clearFormData()
            chart.aa_drawChartWithChartOptions(options)
        })
    }

    private fun configurateCharts(){
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

    private fun loadComparison(){
        try{
            clearJobs()
            dataSize = 0
            initializer.stats.clear()
            initializer.config.config.countriesToCompare.forEach { cntry ->
                    val job = GlobalScope.launch {
                    var refresh = false
                    val covidData = ApiManager.getCovidDataFromApi(initializer.config.config.getDateFromCompare(),
                    DateConverter.formatDateFull(initializer.summaryData.Date), cntry.slug )
                    initializer.stats += CompareCasesStats().apply {
                        if (dataSize==0) dataSize = covidData.size
                        if (abs(dataSize-covidData.size) < 5){
                            if (dataSize<covidData.size) dataSize = covidData.size
                            calculateStats(covidData)
                            country = cntry
                            refresh = true
                        }
                    }
                    if (refresh) configurateCharts()
                    /*delay(2000L)
                    if (this.isActive){
                      this.cancel()
                    }*/
                }
                jobs.plusAssign(job)
            }
        }catch (ex: Exception){ // No action will be taken
            println(ex)
        }
    }

    private fun clearJobs(){
        jobs.forEach { job ->
            if (job.isActive){
                job.cancel()
            }
        }
        jobs.clear()
    }

    override fun applySettings(countries: MutableList<CountryConfig>, daysBack: Long){
        this.initializer.config.config.countriesToCompare = countries
        this.initializer.config.config.daysBackCompare = daysBack
        this.initializer.config.saveConfig()
        daysPicker.value = daysBack.toInt()

        GlobalScope.launch {
            loadComparison()
        }
    }

}