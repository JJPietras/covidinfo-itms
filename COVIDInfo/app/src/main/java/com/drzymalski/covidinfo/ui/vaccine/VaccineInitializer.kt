package com.drzymalski.covidinfo.ui.vaccine

import com.drzymalski.covidinfo.apiUtils.ApiManager
import com.drzymalski.covidinfo.apiUtils.CSVManager
import com.drzymalski.covidinfo.apiUtils.models.DataProvider
import com.drzymalski.covidinfo.apiUtils.models.SummaryData
import com.drzymalski.covidinfo.apiUtils.models.VaccineDay
import com.drzymalski.covidinfo.config.ConfigurationManager
import com.drzymalski.covidinfo.dataUtils.TodayCasesStats
import com.drzymalski.covidinfo.interfaces.DataInitializer
import com.github.aachartmodel.aainfographics.aachartcreator.*
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAOptions
import com.github.aachartmodel.aainfographics.aatools.AAGradientColor
import com.drzymalski.covidinfo.config.backColor
import com.drzymalski.covidinfo.config.fontColor
import com.drzymalski.covidinfo.dataUtils.DateConverter
import com.drzymalski.covidinfo.dataUtils.DateConverter.Companion.formatDateShort
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class VaccineInitializer: DataInitializer {


    var stats = mutableListOf<VaccineDay>()

    var vaccineDoses =  mutableListOf<Float>()
    var vaccinePercentage =  mutableListOf<Float>()

    private val csvManager = CSVManager()
    override val config: ConfigurationManager = ConfigurationManager()

    private fun clearData(){
        vaccineDoses.clear()
        vaccinePercentage.clear()
    }

    @ExperimentalStdlibApi
    fun loadScreenResources(){

        csvManager.loadVaccinationData()
        filterList()
        print("s")
    }

    private fun filterList(){
        stats = csvManager.vaccinationData.filter {it.iso_code == config.config.selectedVaccine.code}.toMutableList()

    }

/*    fun calculateStats(){
        clearData()
        var lastGiven = -1
        var lastPercentage = -1

        stats.forEach { vaccineOnDay ->
            run {
                var temp = vaccineOnDay.people_vaccinated
                if (temp!=null ){
                    if (lastGiven==-1) {lastGiven = temp}
                    else {

                        if (temp <= 0) temp = lastGiven
                        vaccineDoses += temp
                        lastGiven = temp
                    }
                }
            }
        }
    }*/

    fun configureVaccineBarChart(): AAOptions {
        val values = stats.mapNotNull {  day -> day.people_vaccinated } //?: 0
        val dates = stats.mapNotNull { day -> day.date}.toTypedArray()

        val aaChartModel = AAChartModel()
            .chartType(AAChartType.Spline)
            .title("")
            .yAxisTitle("")
            .zoomType(AAChartZoomType.X)
            .markerRadius(0f)
            .categories(dates)
            .backgroundColor(backColor)
            .axesTextColor(fontColor)
            .series(
                arrayOf(
                    AASeriesElement()
                        .name("Zaszczepieni")
                        .lineWidth(4f)
                        .data(values.toTypedArray())
                    .color(AAGradientColor.BerrySmoothie)
                )
            )
        return getChartOptions(aaChartModel)
    }

    fun configureVaccinePercentageBarChart(): AAOptions {
        val values = stats.mapNotNull {  day -> day.people_vaccinated_per_hundred } //?: 0
        val dates = stats.mapNotNull { day -> day.date}.toTypedArray()

        val aaChartModel = AAChartModel()
                .chartType(AAChartType.Spline)
                .title("")
                .yAxisTitle("")
                .zoomType(AAChartZoomType.X)
                .markerRadius(0f)
                .categories(dates)
                .backgroundColor(backColor)
                .axesTextColor(fontColor)
                .series(
                        arrayOf(
                                AASeriesElement()
                                        .name("Procent zaszczepionych")
                                        .lineWidth(4f)
                                        .data(values.toTypedArray())
                                        .color(AAGradientColor.BerrySmoothie)
                        )
                )
        return getChartOptions(aaChartModel)
    }

    fun configureNewVaccinationsBarChart(): AAOptions {
        val values = stats.mapNotNull {  day -> day.daily_vaccinations } //?: 0
        val dates = stats.mapNotNull { day -> day.date}.toTypedArray()

        val aaChartModel = AAChartModel()
                .chartType(AAChartType.Column)
                .title("")
                .yAxisTitle("")
                .zoomType(AAChartZoomType.X)
                .categories(dates)
                .backgroundColor(backColor)
                .axesTextColor(fontColor)
                .series(
                        arrayOf(
                                AASeriesElement()
                                        .name("Ilość szczepień")
                                        .lineWidth(4f)
                                        .data(values.toTypedArray())
                                        .color(AAGradientColor.BerrySmoothie)
                        )
                )
        return getChartOptions(aaChartModel)
    }

   /* fun configureNewCasesBarChart(): AAOptions {

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
    }*/

}




