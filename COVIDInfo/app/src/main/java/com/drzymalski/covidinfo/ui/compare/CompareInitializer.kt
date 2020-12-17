package com.drzymalski.covidinfo.ui.compare

import com.drzymalski.covidinfo.config.backColor
import com.drzymalski.covidinfo.apiUtils.ApiManager
import com.drzymalski.covidinfo.config.ConfigurationManager
import com.drzymalski.covidinfo.config.fontColor
import com.drzymalski.covidinfo.dataUtils.CompareCasesStats
import com.drzymalski.covidinfo.interfaces.DataInitializer
import com.github.aachartmodel.aainfographics.aachartcreator.*
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAOptions


class CompareInitializer: DataInitializer {
    //lateinit var summaryData: SummaryData

    val stats = mutableListOf<CompareCasesStats>()
    var maxLen = 0
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

    fun loadScreenResources(){
        stats.clear()
        val covidData = ApiManager.getCovidDataFromNewApiFromMultipleCountries(config.config.getDateFromCompare(), config.config)
        config.config.countriesToCompare.forEach { country ->
            val countryData = covidData.dataProvider.filter { it.iso3166_1 == country.code }

            val newStats = CompareCasesStats()
            newStats.calculateStats(countryData)
            newStats.country = country

            if (newStats.datesList.size>maxLen) maxLen = newStats.datesList.size

            stats += newStats
        }
    }

    fun normalizeLenghts(){
        val topCnt = stats.find { it.datesList.size == maxLen}
        if (topCnt!=null){
            stats.forEach { countryStat ->
                if (countryStat.datesList.size<maxLen){
                    val diff: Int = maxLen-countryStat.datesList.size
                    countryStat.datesList = topCnt.datesList
                    for (i in 1..diff){
                        countryStat.newCasesWeeklyList.add(0,0f)
                        countryStat.activeCasesList.add(0,0)
                        countryStat.totalCasesList.add(0,0)
                        countryStat.newRecoveredWeeklyList.add(0,0f)
                        countryStat.newDeathsWeeklyList.add(0,0f)
                    }
                }
            }
        }
    }

    fun configureTotalCasesBarChart(): AAOptions {
        val aaChartModel = AAChartModel()
            .chartType(AAChartType.Spline)
            .title("")
            .yAxisTitle("")
            .zoomType(AAChartZoomType.X)
            .markerSymbolStyle(AAChartSymbolStyleType.InnerBlank)
            .markerRadius(0f)
            .backgroundColor(backColor)
            .axesTextColor(fontColor)
            .categories(stats.first().datesList.toTypedArray())
            .series(
                stats.map { stats ->
                    AASeriesElement()
                        .name(stats.country.name)
                        .lineWidth(3f)
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
            .backgroundColor(backColor)
            .axesTextColor(fontColor)
            //.yAxisGridLineWidth(0f)
            //.markerRadius(2f)
            .markerSymbolStyle(AAChartSymbolStyleType.InnerBlank)
            .markerRadius(0f)
            .series(
                stats.map { stats ->
                    AASeriesElement()
                        .name(stats.country.name)
                        .lineWidth(3f)
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
            .backgroundColor(backColor)
            .axesTextColor(fontColor)
            .categories(stats.first().datesList.drop(1).toTypedArray())
            .series(
                stats.map { stats ->
                    AASeriesElement()
                        .name(stats.country.name)
                        .lineWidth(3f)
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
            .backgroundColor(backColor)
            .axesTextColor(fontColor)
            .zoomType(AAChartZoomType.X)
            .series(
                stats.map { stats ->
                    AASeriesElement()
                        .name(stats.country.name)
                        .lineWidth(3f)
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
            .backgroundColor(backColor)
            .axesTextColor(fontColor)
            .series(
                stats.map { stats ->
                    AASeriesElement()
                        .name(stats.country.name)
                        .lineWidth(3f)
                        .color(stats.country.color)
                        .data(stats.activeCasesList.toTypedArray())
                }.toTypedArray()
            )
        return getChartOptions(aaChartModel)
    }

}




