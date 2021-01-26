package com.drzymalski.covidinfo.ui.todayIllness

import com.drzymalski.covidinfo.apiUtils.ApiManager
import com.drzymalski.covidinfo.apiUtils.models.DataProvider
import com.drzymalski.covidinfo.apiUtils.models.SummaryData
import com.drzymalski.covidinfo.config.ConfigurationManager
import com.drzymalski.covidinfo.dataUtils.TodayCasesStats
import com.drzymalski.covidinfo.interfaces.DataInitializer
import com.github.aachartmodel.aainfographics.aachartcreator.*
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAOptions
import com.github.aachartmodel.aainfographics.aatools.AAGradientColor
import com.drzymalski.covidinfo.config.backColor
import com.drzymalski.covidinfo.config.fontColor

class TodayIllnessInitializer: DataInitializer {
    private lateinit var covidData: DataProvider
    //var summaryData: SummaryData? = null
    var newSummaryData: DataProvider? = null

    val stats = TodayCasesStats()

    override val config: ConfigurationManager = ConfigurationManager()

    fun loadMainScreenResources(){
        covidData = ApiManager.getCovidDataFromNewApi(config.config.getDateFromMain(), config.config.selectedCountry.code )
        if (covidData.dataProvider.size < 366)
            stats.calculateStats(covidData)
    }

    fun loadSummaryData(){ //Not used anymore
        try {
            //summaryData = ApiManager.getSummaryFromApi()
            newSummaryData = ApiManager.getCovidDataFromNewApiFromMultipleCountries(config.config.getDateFromSummary(), config.config, summary = true)
           /* summaryData!!.Countries = summaryData!!.Countries.filter{config.config.countries
                .map{ countryConfig -> countryConfig.slug }.contains(it.Slug)}*/
        }catch (ex:Exception){
            println(ex.message) //need to see the errors xd
        }
    }

    fun configureTotalCasesBarChart(): AAOptions {
        val aaChartModel = AAChartModel()
            .chartType(AAChartType.Spline)
            .title("")
            .yAxisTitle("")
            .zoomType(AAChartZoomType.X)
            .markerRadius(0f)
            .categories(stats.datesList.toTypedArray())
            .backgroundColor(backColor)
            .axesTextColor(fontColor)
            .series(
                arrayOf(
                    AASeriesElement()
                        .name("Przypadki potwierdzone")
                        .lineWidth(4f)
                        .data(stats.totalCasesList.toTypedArray())
                    .color(AAGradientColor.BerrySmoothie)
                )
            )
        return getChartOptions(aaChartModel)
    }

    fun configureNewCasesBarChart(): AAOptions {

        val aaChartModel = AAChartModel()
            .chartType(AAChartType.Spline)
            .title("")
            .subtitle("")
            .categories(stats.datesList.drop(1).toTypedArray())
            .yAxisTitle("")
            .zoomType(AAChartZoomType.X)
            .yAxisGridLineWidth(0f)
            .markerRadius(2f)
            .markerSymbolStyle(AAChartSymbolStyleType.InnerBlank)
            .backgroundColor(backColor)
            .axesTextColor(fontColor)
            .series(
                arrayOf(
                    AASeriesElement()
                        .name("Średnia z 7 dni")
                        .lineWidth(2f)
                        .color("rgba(220,20,60,1)")
                        .data(stats.newCasesWeeklyList.toTypedArray()),
                    AASeriesElement()
                        .type(AAChartType.Column)
                        .name("Przypadki")
                        .color(AAGradientColor.ReflexSilver)
                        .data(stats.newCasesList.toTypedArray())
                        .borderWidth(0.0f)
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
            .categories(stats.datesList.drop(1).toTypedArray())
            .backgroundColor(backColor)
            .axesTextColor(fontColor)
            .series(
                arrayOf(
                    AASeriesElement()
                        .name("Ilość zgonów")
                        .lineWidth(2f)
                        .data(stats.newDeathsList.toTypedArray())
                        .color(AAGradientColor.Firebrick)
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
            .categories(stats.datesList.drop(1).toTypedArray())
            .backgroundColor(backColor)
            .axesTextColor(fontColor)
            .series(
                arrayOf(
                    AASeriesElement()
                        .name("Ilość ozdrowień")
                        .lineWidth(2f)
                        .data(stats.newRecoveredList.toTypedArray())
                    .color(AAGradientColor.OceanBlue)
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
            .markerRadius(0f)
            .categories(stats.datesList.toTypedArray())
            .animationType(AAChartAnimationType.Bounce)
            .backgroundColor(backColor)
            .axesTextColor(fontColor)
            .series(
                arrayOf(
                    AASeriesElement()
                        .name("Aktywne przypadki")
                        .lineWidth(3f)
                        .color("rgba(220,20,60,1)")
                        .data(stats.activeCasesList.toTypedArray())
                )
            )
        return getChartOptions(aaChartModel)
    }

}




