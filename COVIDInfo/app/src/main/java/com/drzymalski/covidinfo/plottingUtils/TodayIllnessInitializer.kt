package com.drzymalski.covidinfo.plottingUtils

import com.github.aachartmodel.aainfographics.aachartcreator.*
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAOptions
import com.github.aachartmodel.aainfographics.aatools.AAGradientColor
import java.util.*

class TodayIllnessInitializer {
    val data:DataHolder = DataHolder()

    fun configureTotalCasesBarChart(): AAOptions {
        val aaChartModel = AAChartModel()
            .chartType(AAChartType.Spline)
            .title("")
            .yAxisTitle("")
            .categories(data.datesList.toTypedArray())
            .series(
                arrayOf(
                    AASeriesElement()
                        .name("Przypadki potwierdzone")
                        .lineWidth(4f)
                        .data(data.totalCasesList.toTypedArray())
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
            .categories(data.datesList.drop(1).toTypedArray())
            .yAxisTitle("")
            .yAxisGridLineWidth(0f)
            .markerRadius(2f)
            .markerSymbolStyle(AAChartSymbolStyleType.InnerBlank)
            .series(
                arrayOf(
                    AASeriesElement()
                        .name("Średnia z 7 dni")
                        .lineWidth(2f)
                        .color("rgba(220,20,60,1)")
                        .data(data.newCasesWeeklyList.toTypedArray()),
                    AASeriesElement()
                        .type(AAChartType.Column)
                        .name("Przypadki")
                        .color("#25547c")
                        .data(data.newCasesList.toTypedArray())
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
            .categories(data.datesList.drop(1).toTypedArray())
            .series(
                arrayOf(
                    AASeriesElement()
                        .name("Ilość zgonów")
                        .lineWidth(2f)
                        .data(data.newDeathsList.toTypedArray())
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
            .categories(data.datesList.drop(1).toTypedArray())
            .series(
                arrayOf(
                    AASeriesElement()
                        .name("Ilość ozdrowień")
                        .lineWidth(2f)
                        .data(data.newRecoveredList.toTypedArray())
                    .color(AAGradientColor.oceanBlueColor())
                )
            )
        return getChartOptions(aaChartModel)
    }

    fun configureActiveBarChart(): AAOptions {
        val aaChartModel = AAChartModel()
            .chartType(AAChartType.Spline)
            .title("")
            .yAxisTitle("")
            .markerSymbolStyle(AAChartSymbolStyleType.InnerBlank)
            .categories(data.datesList.toTypedArray())
            .animationType(AAChartAnimationType.Bounce)
            .series(
                arrayOf(
                    AASeriesElement()
                        .name("Aktywne przypadki")
                        .lineWidth(4f)
                        .color("rgba(220,20,60,1)")
                        .data(data.activeCasesList.toTypedArray())
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

    }

}




