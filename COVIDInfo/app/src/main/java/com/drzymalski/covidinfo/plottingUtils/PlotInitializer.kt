package com.drzymalski.covidinfo.plottingUtils

import android.content.Context
import com.drzymalski.covidinfo.apiUtils.ApiManager.Companion.getCovidDataFromApi
import com.drzymalski.covidinfo.apiUtils.Models.CovidDay
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartSymbolStyleType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.github.aachartmodel.aainfographics.aatools.AAGradientColor
import java.io.IOException

class PlotInitializer {

    private var covidData: List<CovidDay>  = mutableListOf()

    private val totalCasesList = mutableListOf<Int>()
    private val totalDeathsList = mutableListOf<Int>()
    private val totalRecoveredList = mutableListOf<Int>()

    private val activeCasesList = mutableListOf<Int>()

    private val newCasesList = mutableListOf<Int>()

    private val datesList = mutableListOf<String>()

    fun loadMainScreenResouces(dateFrom: String, dateTo: String){
        clearData()
        var last = 0
        covidData= getCovidDataFromApi(dateFrom, dateTo)
        covidData.forEach { casesOnDay ->
            run {
                totalCasesList += casesOnDay.Confirmed
                totalDeathsList += casesOnDay.Deaths
                totalRecoveredList += casesOnDay.Recovered

                activeCasesList += casesOnDay.Active
                datesList += casesOnDay.Date.day.toString()

                if (last==0) {last = casesOnDay.Confirmed}
                else {
                    newCasesList += (casesOnDay.Confirmed - last)
                    last = casesOnDay.Confirmed
                }
            }
        }
    }

    private fun clearData(){
        totalCasesList.clear()
        totalDeathsList.clear()
        totalRecoveredList.clear()

        activeCasesList.clear()

        datesList.clear()
        newCasesList.clear()
    }

    fun configureTotalCasesBarChart(): AAChartModel {
        return AAChartModel()
            .chartType(AAChartType.Column)
            .title("")
            .yAxisTitle("")
            .dataLabelsEnabled(false)
            .tooltipEnabled(false)
            .markerRadius(0f)
            .xAxisVisible(true)
            .yAxisVisible(true)
            .series(
                arrayOf(
                    AASeriesElement()
                        .name("COVIDIUM TOTAL Data")
                        .lineWidth(2f)
                        .data(totalCasesList.toTypedArray())
                    .color(AAGradientColor.berrySmoothieColor())
                )
            )
    }

    fun configureNewCasesBarChart(): AAChartModel {
        return AAChartModel()
            .chartType(AAChartType.Spline)
            .title("")
            .yAxisTitle("")
            .dataLabelsEnabled(false)
            .tooltipEnabled(false)
            .markerRadius(0f)
            .xAxisVisible(true)
            .yAxisVisible(true)
            .series(
                arrayOf(
                    AASeriesElement()
                        .name("7 day avg")
                        .lineWidth(5.0f)
                        .color("rgba(220,20,60,1)")
                        .data(newCasesList.toTypedArray()),
                    AASeriesElement()
                        .type(AAChartType.Column)
                        .name("Cases")
                        .color("#25547c")
                        .data(newCasesList.toTypedArray())
                )
            )
    }

    fun configureDeathsBarChart(): AAChartModel {
        return AAChartModel()
            .chartType(AAChartType.Column)
            .title("")
            .yAxisTitle("")
            .dataLabelsEnabled(false)
            .tooltipEnabled(false)
            .markerRadius(0f)
            .xAxisVisible(true)
            .yAxisVisible(true)
            .series(
                arrayOf(
                    AASeriesElement()
                        .name("COVIDIUM DEATHS Data")
                        .lineWidth(2f)
                        .data(totalDeathsList.toTypedArray())
                    .color(AAGradientColor.firebrickColor())
                )
            )
    }

    fun configureRecoveredBarChart(): AAChartModel {
        return AAChartModel()
            .chartType(AAChartType.Column)
            .title("")
            .yAxisTitle("")
            .dataLabelsEnabled(false)
            .tooltipEnabled(false)
            .markerRadius(0f)
            .xAxisVisible(true)
            .yAxisVisible(true)
            .series(
                arrayOf(
                    AASeriesElement()
                        .name("COVIDIUM RECOVERED Data")
                        .lineWidth(2f)
                        .data(totalRecoveredList.toTypedArray())
                    .color(AAGradientColor.oceanBlueColor())
                )
            )
    }

    fun configureAvtiveBarChart(): AAChartModel {
        return AAChartModel()
            .chartType(AAChartType.Spline)
            .title("")
            .yAxisTitle("")
            .dataLabelsEnabled(false)
            .tooltipEnabled(false)
            .markerRadius(0f)
            .xAxisVisible(true)
            .yAxisVisible(true)
            .series(
                arrayOf(
                    AASeriesElement()
                        .type(AAChartType.Spline)
                        .name("7 day avg")
                        .lineWidth(5.0f)
                        .color("rgba(220,20,60,1)")
                        .data(activeCasesList.toTypedArray()),
                    AASeriesElement()
                        .type(AAChartType.Column)
                        .name("Active")
                        .color("#25547c")
                        .data(activeCasesList.toTypedArray())
                )
            )
    }


    companion object {

        fun configureAAChartModel2(): AAChartModel {

            return AAChartModel()
                .chartType(AAChartType.Spline)
                .title("")
                .subtitle("")
                .categories(
                    arrayOf(
                        "Jan",
                        "Feb",
                        "Mar",
                        "Apr",
                        "May",
                        "Jun",
                        "July",
                        "Aug",
                        "Spe",
                        "Oct",
                        "Nov",
                        "Dec"
                    )
                )
                .yAxisTitle("")
                .yAxisGridLineWidth(0f)
                .markerRadius(4f)
                .markerSymbolStyle(AAChartSymbolStyleType.InnerBlank)
                .series(
                    arrayOf(
                        AASeriesElement()
                            .name("7 day avg")
                            .lineWidth(5.0f)
                            .color("rgba(220,20,60,1)")
                            .data(
                                arrayOf(
                                    7.0,
                                    6.9,
                                    2.5,
                                    14.5,
                                    18.2,
                                    21.5,
                                    5.2,
                                    26.5,
                                    23.3,
                                    45.3,
                                    13.9,
                                    9.6
                                )
                            ),
                        AASeriesElement()
                            .type(AAChartType.Column)
                            .name("Cases")
                            .color("#25547c")
                            .data(
                                arrayOf(
                                    7.0,
                                    6.9,
                                    2.5,
                                    14.5,
                                    18.2,
                                    21.5,
                                    5.2,
                                    26.5,
                                    23.3,
                                    45.3,
                                    13.9,
                                    9.6
                                )
                            )
                    )
                )
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
    }
}