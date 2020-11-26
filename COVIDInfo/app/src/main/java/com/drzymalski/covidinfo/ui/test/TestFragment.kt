package com.drzymalski.covidinfo.ui.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.drzymalski.covidinfo.R
import com.drzymalski.covidinfo.plottingUtils.TodayIllnessInitializer
import com.github.aachartmodel.aainfographics.aachartcreator.*

class TestFragment : Fragment() {

    private lateinit var testViewModel: TestViewModel
    private var plotInitialiserTest: TodayIllnessInitializer = TodayIllnessInitializer()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        testViewModel =
                ViewModelProviders.of(this).get(TestViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_test, container, false)
        val textView: TextView = root.findViewById(R.id.text_test)

        plotInitialiserTest.data.loadMainScreenResouces()

        val aaChartView = root.findViewById<AAChartView>(R.id.aa_chart_view)
        aaChartView.aa_drawChartWithChartOptions(plotInitialiserTest.configureTotalCasesBarChart())

        val aaChartView2 = root.findViewById<AAChartView>(R.id.aa_chart_view2)
        aaChartView2.aa_drawChartWithChartModel(TodayIllnessInitializer.configureAAChartModel2())

        testViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

}