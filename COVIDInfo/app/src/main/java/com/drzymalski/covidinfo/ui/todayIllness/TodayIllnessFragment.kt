package com.drzymalski.covidinfo.ui.todayIllness

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.drzymalski.covidinfo.R
import com.drzymalski.covidinfo.plottingUtils.TodayIllnessInitializer
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView

class TodayIllnessFragment : Fragment() {

    private lateinit var todayIllnessViewModel: TodayIllnessViewModel

    private  var todayIllnessInitializer: TodayIllnessInitializer = TodayIllnessInitializer()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        todayIllnessViewModel =
                ViewModelProviders.of(this).get(TodayIllnessViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_today, container, false)
        todayIllnessViewModel.text.observe(viewLifecycleOwner, Observer { })

        todayIllnessInitializer.data.loadMainScreenResouces("2020-10-01", "2020-11-12")

        val aaChartViewNewCases = root.findViewById<AAChartView>(R.id.aaChartViewNewCases)

        aaChartViewNewCases.aa_drawChartWithChartOptions(todayIllnessInitializer.configureNewCasesBarChart())

        val aaChartViewTotalCases = root.findViewById<AAChartView>(R.id.aaChartViewTotalCases)
        aaChartViewTotalCases.aa_drawChartWithChartOptions(todayIllnessInitializer.configureTotalCasesBarChart())

        val aaChartActiveTotalCases = root.findViewById<AAChartView>(R.id.aaChartViewActiveCases)
        aaChartActiveTotalCases.aa_drawChartWithChartOptions(todayIllnessInitializer.configureAvtiveBarChart())

        val aaChartRecovered = root.findViewById<AAChartView>(R.id.aaChartViewRecovered)
        aaChartRecovered.aa_drawChartWithChartOptions(todayIllnessInitializer.configureRecoveredBarChart())

        val aaChartDeadlyCases = root.findViewById<AAChartView>(R.id.aaChartViewDied)
        aaChartDeadlyCases.aa_drawChartWithChartOptions(todayIllnessInitializer.configureDeathsBarChart())


        return root
    }
}