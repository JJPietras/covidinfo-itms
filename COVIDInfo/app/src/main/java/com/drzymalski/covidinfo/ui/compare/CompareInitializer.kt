package com.drzymalski.covidinfo.ui.compare

import com.drzymalski.covidinfo.apiUtils.ApiManager
import com.drzymalski.covidinfo.apiUtils.models.SummaryData
import com.drzymalski.covidinfo.config.ConfigurationManager
import com.drzymalski.covidinfo.dataUtils.CompareCasesStats
import com.drzymalski.covidinfo.interfaces.DataInitializer
import com.github.aachartmodel.aainfographics.aachartcreator.*
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAOptions
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAScrollablePlotArea
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAStyle
import com.github.aachartmodel.aainfographics.aatools.AAColor

class CompareInitializer: DataInitializer {
    //lateinit var summaryData: SummaryData

    val stats = mutableListOf<CompareCasesStats>()

    override val config: ConfigurationManager = ConfigurationManager()

    /*fun loadScreenResources(){
        try {
            summaryData = ApiManager.getSummaryFromApi()
            summaryData.Countries = summaryData.Countries.filter{config.config.countries
                .map{ countryConfig -> countryConfig.slug }.contains(it.Slug)}
        }catch (ex:Exception){
            println(ex.message) //need to see the errors xd
        }
    }*/

    fun configureTotalCasesBarChart(): AAOptions {
        val aaChartModel = AAChartModel()
            .chartType(AAChartType.Spline)
            .title("")
            .yAxisTitle("")
            .zoomType(AAChartZoomType.X)
            .markerSymbolStyle(AAChartSymbolStyleType.InnerBlank)
            .markerRadius(0f)
            .categories(stats.first().datesList.toTypedArray())
            .series(
                stats.map { stats ->
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
            .categories(stats.first().datesList.drop(1).toTypedArray())
            .yAxisTitle("")
            .zoomType(AAChartZoomType.X)
            .yAxisGridLineWidth(0f)
            //.markerRadius(2f)
            .markerSymbolStyle(AAChartSymbolStyleType.InnerBlank)
            .markerRadius(0f)
            .series(
                stats.map { stats ->
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
            .categories(stats.first().datesList.drop(1).toTypedArray())
            .series(
                stats.map { stats ->
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
            .categories(stats.first().datesList.drop(1).toTypedArray())
            .markerSymbolStyle(AAChartSymbolStyleType.InnerBlank)
            .markerRadius(0f)
            .zoomType(AAChartZoomType.X)
            .series(
                stats.map { stats ->
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
            .categories(stats.first().datesList.toTypedArray())
            .animationType(AAChartAnimationType.Bounce)
            .zoomType(AAChartZoomType.X)
            .series(
                stats.map { stats ->
                    AASeriesElement()
                        .name(stats.country.name)
                        .lineWidth(2f)
                        .color(stats.country.color)
                        .data(stats.activeCasesList.toTypedArray())
                }.toTypedArray()
            )
        return getChartOptions(aaChartModel)
    }

}




