package com.drzymalski.covidinfo.ui.twitter

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.drzymalski.covidinfo.R
import com.drzymalski.covidinfo.ui.test.CovidDay
import com.github.aachartmodel.aainfographics.aachartcreator.*
import com.github.aachartmodel.aainfographics.aatools.AAGradientColor
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

class TestFragment : Fragment() {

    private lateinit var testViewModel: TestViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        testViewModel =
                ViewModelProviders.of(this).get(TestViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_test, container, false)
        val textView: TextView = root.findViewById(R.id.text_test)
        val aaChartView = root.findViewById<AAChartView>(R.id.aa_chart_view)
        aaChartView.aa_drawChartWithChartModel(configureAAChartModel())

        testViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

    private fun configureAAChartModel(): AAChartModel {

        val jsonFileString = this.context?.let { getJsonDataFromAsset(it,"plot_data2.json") }
        Log.i("data", jsonFileString)

        val gson = Gson()
        val listPersonType = object : TypeToken<List<CovidDay>>() {}.type

        val persons: List<CovidDay> = gson.fromJson(jsonFileString, listPersonType)
        val someList = mutableListOf(0)

        persons.forEach{ person -> someList += person.Cases }


        return AAChartModel()
            .chartType(AAChartType.Column)
            .title("")
            .yAxisTitle("")
            .dataLabelsEnabled(false)
            .tooltipEnabled(false)
            .markerRadius(0f)
            .xAxisVisible(false)
            .yAxisVisible(false)
            .series(
                arrayOf(
                    AASeriesElement()
                        .name("COVIDIUM Data")
                        .lineWidth(2f)
                        .data(
                            someList.toTypedArray()
                        )
                        .color(AAGradientColor.oceanBlueColor())
                )
            )
    }

}