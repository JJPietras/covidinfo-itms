package com.drzymalski.covidinfo.plottingUtils

import android.content.Context
import com.drzymalski.covidinfo.apiUtils.ApiManager.Companion.getCovidDataFromApi
import com.drzymalski.covidinfo.apiUtils.Models.CovidDay
import com.github.aachartmodel.aainfographics.aachartcreator.*
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AADataLabels
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AALabel
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAOptions
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAStyle
import com.github.aachartmodel.aainfographics.aatools.AAColor
import com.github.aachartmodel.aainfographics.aatools.AAGradientColor
import java.io.IOException
import java.lang.ref.PhantomReference

class PlotInitializer {

    private var covidData: List<CovidDay>  = mutableListOf()

    private val totalCasesList = mutableListOf<Int>()

    private val newDeathsList = mutableListOf<Int>()
    private val newRecoveredList = mutableListOf<Int>()

    private val activeCasesList = mutableListOf<Int>()

    private val newCasesList = mutableListOf<Int>()

    private val datesList = mutableListOf<String>()

    fun loadMainScreenResouces(dateFrom: String, dateTo: String){
        clearData()
        var lastCases = 0
        var lastDeaths = 0
        var lastRecovered = 0
        covidData= getCovidDataFromApi(dateFrom, dateTo)
        covidData.forEach { casesOnDay ->
            run {
                totalCasesList += casesOnDay.Confirmed
                //newDeathsList += casesOnDay.Deaths
                //newRecoveredList += casesOnDay.Recovered

                activeCasesList += casesOnDay.Active
                @Suppress("DEPRECATION")
                datesList += casesOnDay.Date.toString()

                if (lastDeaths==0) {lastDeaths = casesOnDay.Deaths}
                else {
                    newDeathsList += (casesOnDay.Deaths - lastDeaths)
                    lastDeaths = casesOnDay.Deaths
                }

                if (lastRecovered==0) {lastRecovered = casesOnDay.Recovered}
                else {
                    newRecoveredList += (casesOnDay.Recovered - lastRecovered)
                    lastRecovered = casesOnDay.Recovered
                }

                if (lastCases==0) {lastCases = casesOnDay.Confirmed}
                else {
                    newCasesList += (casesOnDay.Confirmed - lastCases)
                    lastCases = casesOnDay.Confirmed
                }
            }
        }
    }

    private fun clearData(){
        totalCasesList.clear()
        newDeathsList.clear()
        newRecoveredList.clear()

        activeCasesList.clear()

        datesList.clear()
        newCasesList.clear()
    }

    fun configureTotalCasesBarChart(): AAOptions {
        val aaChartModel = AAChartModel()
            .chartType(AAChartType.Spline)
            .title("")
            .yAxisTitle("")
            .categories(datesList.toTypedArray())
            .series(
                arrayOf(
                    AASeriesElement()
                        .name("Przypadki potwierdzone")
                        .lineWidth(2f)
                        .data(totalCasesList.toTypedArray())
                    .color(AAGradientColor.berrySmoothieColor())
                )
            )
        return getChartOptions(aaChartModel)
    }

    fun configureNewCasesBarChart(): AAOptions {

        val aaChartModel = AAChartModel()
            .chartType(AAChartType.Spline)
            .title("")
            .subtitle("")
            .categories(datesList.drop(1).toTypedArray())
            .yAxisTitle("")
            .yAxisGridLineWidth(0f)
            .markerRadius(2f)
            .markerSymbolStyle(AAChartSymbolStyleType.InnerBlank)
            .series(
                arrayOf(
                    AASeriesElement()
                        .name("Średnia z 7 dni")
                        .lineWidth(2.0f)
                        .color("rgba(220,20,60,1)")
                        .data(newCasesList.toTypedArray()),
                    AASeriesElement()
                        .type(AAChartType.Column)
                        .name("Przypadki")
                        .color("#25547c")
                        .data(newCasesList.toTypedArray())
                )
            )
        return getChartOptions(aaChartModel)
    }

    fun configureDeathsBarChart(): AAOptions {
        val aaChartModel = AAChartModel()
            .chartType(AAChartType.Column)
            .title("")
            .yAxisTitle("")
            .markerRadius(0f)
            .categories(datesList.drop(1).toTypedArray())
            .series(
                arrayOf(
                    AASeriesElement()
                        .name("Ilość zgonów")
                        .lineWidth(2f)
                        .data(newDeathsList.toTypedArray())
                        .color(AAGradientColor.firebrickColor())
                )
            )
        return getChartOptions(aaChartModel)
    }

    fun configureRecoveredBarChart(): AAOptions {
        val aaChartModel = AAChartModel()
            .chartType(AAChartType.Column)
            .title("")
            .yAxisTitle("")
            .categories(datesList.drop(1).toTypedArray())
            .series(
                arrayOf(
                    AASeriesElement()
                        .name("Ilość ozdrowień")
                        .lineWidth(2f)
                        .data(newRecoveredList.toTypedArray())
                    .color(AAGradientColor.oceanBlueColor())
                )
            )
        return getChartOptions(aaChartModel)
    }

    fun configureAvtiveBarChart(): AAOptions {
        val aaChartModel = AAChartModel()
            .chartType(AAChartType.Spline)
            .title("")
            .yAxisTitle("")
            .markerSymbolStyle(AAChartSymbolStyleType.InnerBlank)
            .categories(datesList.toTypedArray())
            .animationType(AAChartAnimationType.Bounce)
            .series(
                arrayOf(
                    AASeriesElement()
                        .name("Aktywne przypadki")
                        .lineWidth(5.0f)
                        .color("rgba(220,20,60,1)")
                        .data(activeCasesList.toTypedArray())
                )
            )
        return getChartOptions(aaChartModel)
    }

    private fun getChartOptions(aaChartModel: AAChartModel): AAOptions {
        val aaOptions = AAOptionsConstructor.configureChartOptions(aaChartModel)
        aaOptions.tooltip!!
            .shared(true)
            /*.useHTML(true)
            .headerFormat("<small style=\\\"color: brown;\\\">{point.key}</small><table style=\\\"color: brown;\\\">")
            .pointFormat(
                "<tr><td><li></li></td><td><small>{point.series.name}: </small></td> <td><small>{point.y}</small></td></tr>"
            )
            .footerFormat("</table>")*/
            //.backgroundColor(AAColor.grayColor())
            .valueDecimals(0)
        return aaOptions
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