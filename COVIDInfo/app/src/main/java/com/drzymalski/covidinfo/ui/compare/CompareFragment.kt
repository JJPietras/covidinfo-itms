package com.drzymalski.covidinfo.ui.compare

import android.annotation.SuppressLint
import android.app.ActionBar
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity.CENTER
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.drzymalski.covidinfo.R
import com.drzymalski.covidinfo.apiUtils.ApiManager
import com.drzymalski.covidinfo.lib.FragmentBinder
import com.drzymalski.covidinfo.config.CountryConfig
import com.drzymalski.covidinfo.dataUtils.CompareCasesStats
import com.drzymalski.covidinfo.dataUtils.DateConverter
import com.drzymalski.covidinfo.ui.selector.SelectorFragment
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAOptions
import kotlinx.android.synthetic.main.fragment_compare.aaChartViewActiveCases
import kotlinx.android.synthetic.main.fragment_compare.aaChartViewDied
import kotlinx.android.synthetic.main.fragment_compare.aaChartViewNewCases
import kotlinx.android.synthetic.main.fragment_compare.aaChartViewRecovered
import kotlinx.android.synthetic.main.fragment_compare.aaChartViewTotalCases
import kotlinx.android.synthetic.main.fragment_compare.statisticsChangeCountryBtn
import kotlinx.android.synthetic.main.fragment_compare.statisticsCountriesLayout
import kotlinx.android.synthetic.main.fragment_compare.statisticsReload
import kotlinx.android.synthetic.main.fragment_compare.daysPicker
import kotlinx.coroutines.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.abs


class CompareFragment : Fragment() {

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

        GlobalScope.launch {
            initializer.data.loadScreenResources()
            loadComparison()
            generateCountryButtons()
        }

        statisticsChangeCountryBtn.setOnClickListener{
            if (statisticsCountriesLayout.visibility == View.VISIBLE)
                statisticsCountriesLayout.visibility = View.GONE
            else
                statisticsCountriesLayout.visibility = View.VISIBLE
        }

        FragmentBinder.bindToButton(
            view.findViewById(R.id.statisticsMenuBtn),
            SelectorFragment(),
            requireActivity()
        )

        statisticsReload.setOnClickListener {
            initializer.data.config.config.daysBackCompare = daysPicker.value.toLong()
            initializer.data.config.saveConfig()
            loadComparison()
            daysChanged = false
        }
        daysPicker.minValue = 7
        daysPicker.maxValue = 365
        daysPicker.value = initializer.data.config.config.daysBackCompare.toInt()
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


    private fun generateCountryButtons(){
        try{
            initializer.data.config.config.countries.forEach{ countryConfig ->
                val data = initializer.data.summaryData.Countries.find { countryConfig.slug == it.Slug }
                if (data != null){
                    val button = Button(this.context).apply {
                        layoutParams = LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                            ActionBar.LayoutParams.WRAP_CONTENT, 0.6f)
                        val shape = GradientDrawable()

                        shape.cornerRadius = 100f
                        background = shape
                        //setBackgroundResource(R.drawable.sphere) //Looks stretched when you rotate or have a weird aspect ratio
                        textSize = 22f
                        text = data.CountryCode
                        setTextColor(Color.parseColor("#FFFFFF"))
                        backgroundTintList = ColorStateList.valueOf(Color.parseColor(countryConfig.color))
                        setOnClickListener{
                            statisticsCountriesLayout.visibility = View.GONE
                            changeCountry(countryConfig)
                        }
                    }

                    val parent = LinearLayout(context).apply {
                        layoutParams = LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                            ActionBar.LayoutParams.WRAP_CONTENT, 0.2f)
                        orientation = LinearLayout.HORIZONTAL
                        setPadding(20,5,5,5)
                    }

                    val child = LinearLayout(context).apply {
                        layoutParams = LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                            ActionBar.LayoutParams.WRAP_CONTENT, 0.4f)
                        orientation = LinearLayout.VERTICAL
                        setPadding(30,0, 30,0)
                    }

                    val tvCountry = TextView(this.context).apply {
                        textSize = 20f
                        text = countryConfig.name
                        setTextColor(Color.parseColor("#373737"))
                    }

                    val tvContinent = TextView(this.context).apply {
                        textSize = 14f
                        text = countryConfig.continent
                    }

                    val tvCases = TextView(this.context).apply {
                        layoutParams = LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                            ActionBar.LayoutParams.WRAP_CONTENT, 0.4f)
                        gravity = CENTER
                        textSize = 20f
                        text = "${data.NewConfirmed} zakażeń"
                    }

                    child.addView(tvCountry)
                    child.addView(tvContinent)

                    parent.addView(button)
                    parent.addView(child)
                    parent.addView(tvCases)

                    statisticsCountriesLayout.post(Runnable {
                        statisticsCountriesLayout.addView(parent)
                    })
                }
            }
        }catch (ex: Exception){ // No action will be taken
            println(ex)
        }
    }

    private fun loadComparison(){
        try{
            clearJobs()
            dataSize = 0
            initializer.data.stats.clear()
            initializer.data.config.config.countriesToCompare.forEach { cntry ->
                    val job = GlobalScope.launch {
                    var refresh = false
                    val covidData = ApiManager.getCovidDataFromApi(initializer.data.config.config.getDateFromCompare(),
                    DateConverter.formatDateFull(initializer.data.summaryData.Date), cntry.slug )
                    initializer.data.stats += CompareCasesStats().apply {
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

    private fun changeCountry(countryConfig: CountryConfig){
        initializer.data.config.config.selectedCountry = countryConfig

        GlobalScope.launch {
            initializer.data.loadScreenResources()
            loadComparison()
            generateCountryButtons()
        }
    }

}