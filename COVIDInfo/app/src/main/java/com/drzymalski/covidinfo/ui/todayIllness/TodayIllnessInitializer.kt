package com.drzymalski.covidinfo.ui.todayIllness

import com.github.aachartmodel.aainfographics.aachartcreator.*
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAOptions
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAScrollablePlotArea
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAStyle
import com.github.aachartmodel.aainfographics.aatools.AAColor
import com.github.aachartmodel.aainfographics.aatools.AAGradientColor

class TodayIllnessInitializer {
    val data: MainScreenData = MainScreenData()

    fun configureTotalCasesBarChart(): AAOptions {
        val aaChartModel = AAChartModel()
            .chartType(AAChartType.Spline)
            .title("")
            .yAxisTitle("")
            .zoomType(AAChartZoomType.X)
            .categories(data.stats.datesList.toTypedArray())
            .series(
                arrayOf(
                    AASeriesElement()
                        .name("Przypadki potwierdzone")
                        .lineWidth(4f)
                        .data(data.stats.totalCasesList.toTypedArray())
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
            .categories(data.stats.datesList.drop(1).toTypedArray())
            .yAxisTitle("")
            .zoomType(AAChartZoomType.X)
            .yAxisGridLineWidth(0f)
            .markerRadius(2f)
            .markerSymbolStyle(AAChartSymbolStyleType.InnerBlank)
            .series(
                arrayOf(
                    AASeriesElement()
                        .name("Średnia z 7 dni")
                        .lineWidth(2f)
                        .color("rgba(220,20,60,1)")
                        .data(data.stats.newCasesWeeklyList.toTypedArray()),
                    AASeriesElement()
                        .type(AAChartType.Column)
                        .name("Przypadki")
                        .color("#25547c")
                        .data(data.stats.newCasesList.toTypedArray())
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
            .zoomType(AAChartZoomType.X)
            .categories(data.stats.datesList.drop(1).toTypedArray())
            .series(
                arrayOf(
                    AASeriesElement()
                        .name("Ilość zgonów")
                        .lineWidth(2f)
                        .data(data.stats.newDeathsList.toTypedArray())
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
            .zoomType(AAChartZoomType.X)
            .categories(data.stats.datesList.drop(1).toTypedArray())
            .series(
                arrayOf(
                    AASeriesElement()
                        .name("Ilość ozdrowień")
                        .lineWidth(2f)
                        .data(data.stats.newRecoveredList.toTypedArray())
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
            .zoomType(AAChartZoomType.X)
            .markerSymbolStyle(AAChartSymbolStyleType.InnerBlank)
            .categories(data.stats.datesList.toTypedArray())
            .animationType(AAChartAnimationType.Bounce)

            .series(
                arrayOf(
                    AASeriesElement()
                        .name("Aktywne przypadki")
                        .lineWidth(4f)
                        .color("rgba(220,20,60,1)")
                        .data(data.stats.activeCasesList.toTypedArray())
                )
            )
        return getChartOptions(aaChartModel)
    }

    private fun getChartOptions(aaChartModel: AAChartModel): AAOptions {
        val aaOptions = AAOptionsConstructor.configureChartOptions(aaChartModel)
        aaOptions.tooltip!!
            .shared(true)

            .style(AAStyle().color(AAColor.blackColor()))
            .backgroundColor(AAColor.rgbaColor(180, 180, 180, 0.9f))
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




