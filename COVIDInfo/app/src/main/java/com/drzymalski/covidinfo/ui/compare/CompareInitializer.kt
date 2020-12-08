package com.drzymalski.covidinfo.ui.compare

import com.github.aachartmodel.aainfographics.aachartcreator.*
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAOptions
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAScrollablePlotArea
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAStyle
import com.github.aachartmodel.aainfographics.aatools.AAColor

class CompareInitializer {
    val data = CompareScreenData()

    fun configureTotalCasesBarChart(): AAOptions {
        val aaChartModel = AAChartModel()
            .chartType(AAChartType.Spline)
            .title("")
            .yAxisTitle("")
            .zoomType(AAChartZoomType.X)
            .markerSymbolStyle(AAChartSymbolStyleType.InnerBlank)
            .markerRadius(0f)
            .categories(data.stats.first().datesList.toTypedArray())
            .series(
                data.stats.map { stats ->
                    AASeriesElement()
                        .name(stats.country.name)
                        .lineWidth(2f)
                        .color(stats.country.color)
                        .data(stats.totalCasesList.toTypedArray())
                }.toTypedArray()
            )

        return getChartOptions(aaChartModel)
    }

    fun configureNewCasesBarChart(): AAOptions {

        val aaChartModel = AAChartModel()
            .chartType(AAChartType.Spline)
            .title("")
            .subtitle("")
            .categories(data.stats.first().datesList.drop(1).toTypedArray())
            .yAxisTitle("")
            .zoomType(AAChartZoomType.X)
            .yAxisGridLineWidth(0f)
            //.markerRadius(2f)
            .markerSymbolStyle(AAChartSymbolStyleType.InnerBlank)
            .markerRadius(0f)
            .series(
                data.stats.map { stats ->
                    AASeriesElement()
                        .name(stats.country.name)
                        .lineWidth(2f)
                        .color(stats.country.color)
                        .data(stats.newCasesWeeklyList.toTypedArray())
                }.toTypedArray()
            )
        return getChartOptions(aaChartModel)
    }

    fun configureDeathsBarChart(): AAOptions {
        val aaChartModel = AAChartModel()
            .chartType(AAChartType.Spline)
            .title("")
            .yAxisTitle("")
            .zoomType(AAChartZoomType.X)
            .markerSymbolStyle(AAChartSymbolStyleType.InnerBlank)
            .markerRadius(0f)
            .categories(data.stats.first().datesList.drop(1).toTypedArray())
            .series(
                data.stats.map { stats ->
                    AASeriesElement()
                        .name(stats.country.name)
                        .lineWidth(2f)
                        .color(stats.country.color)
                        .data(stats.newDeathsWeeklyList.toTypedArray())
                }.toTypedArray()
            )
        return getChartOptions(aaChartModel)
    }

    fun configureRecoveredBarChart(): AAOptions {
        val aaChartModel = AAChartModel()
            .chartType(AAChartType.Spline)
            .title("")
            .yAxisTitle("")
            .categories(data.stats.first().datesList.drop(1).toTypedArray())
            .markerSymbolStyle(AAChartSymbolStyleType.InnerBlank)
            .markerRadius(0f)
            .zoomType(AAChartZoomType.X)
            .series(
                data.stats.map { stats ->
                    AASeriesElement()
                        .name(stats.country.name)
                        .lineWidth(2f)
                        .color(stats.country.color)
                        .data(stats.newRecoveredWeeklyList.toTypedArray())
                }.toTypedArray()
            )
        return getChartOptions(aaChartModel)
    }

    fun configureActiveBarChart(): AAOptions {
        val aaChartModel = AAChartModel()
            .chartType(AAChartType.Spline)
            .title("")
            .yAxisTitle("")
            .markerSymbolStyle(AAChartSymbolStyleType.InnerBlank)
            .markerRadius(0f)
            .categories(data.stats.first().datesList.toTypedArray())
            .animationType(AAChartAnimationType.Bounce)
            .zoomType(AAChartZoomType.X)
            .series(
                data.stats.map { stats ->
                    AASeriesElement()
                        .name(stats.country.name)
                        .lineWidth(2f)
                        .color(stats.country.color)
                        .data(stats.activeCasesList.toTypedArray())
                }.toTypedArray()
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

}




