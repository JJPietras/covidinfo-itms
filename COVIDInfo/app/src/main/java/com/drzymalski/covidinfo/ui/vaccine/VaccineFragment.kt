package com.drzymalski.covidinfo.ui.vaccine

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.drzymalski.covidinfo.R
import com.drzymalski.covidinfo.apiUtils.CSVManager
import com.drzymalski.covidinfo.apiUtils.models.VaccineDay
import com.drzymalski.covidinfo.lib.FragmentBinder
import com.drzymalski.covidinfo.ui.selector.SelectorFragment
import com.drzymalski.covidinfo.ui.todayIllness.TodayIllnessInitializer
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAOptions
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.vhl.blackmo.grass.dsl.grass

import kotlinx.android.synthetic.main.fragment_vaccine.aaChartViewVaccinations
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL


class VaccineFragment : Fragment() {

    private lateinit var vaccineViewModel: VaccineViewModel
    private var initializer: VaccineInitializer = VaccineInitializer()

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

        refreshData()
        /*val texts = arrayOf(icon1, icon2, icon3, icon4, icon5, icon6, icon7, icon8)
        for (text in texts) text.movementMethod = LinkMovementMethod.getInstance()

        */
    }

    private fun configureChart(chart: AAChartView, options: AAOptions) {
        chart.post(kotlinx.coroutines.Runnable { chart.aa_drawChartWithChartOptions(options) })
    }

    private fun configureCharts(){
        try {
            configureChart(aaChartViewVaccinations, initializer.configureVaccineBarChart())
            /*configureChart(aaChartViewTotalCases, initializer.configureTotalCasesBarChart())
            configureChart(aaChartViewActiveCases, initializer.configureActiveBarChart())
            configureChart(aaChartViewRecovered, initializer.configureRecoveredBarChart())
            configureChart(aaChartViewDied, initializer.configureDeathsBarChart())*/
        }catch (ex: Exception){ // No action will be taken
            println(ex)
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
}