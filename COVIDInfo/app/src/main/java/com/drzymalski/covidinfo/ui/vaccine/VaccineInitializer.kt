package com.drzymalski.covidinfo.ui.vaccine

import com.drzymalski.covidinfo.apiUtils.CSVManager
import com.drzymalski.covidinfo.apiUtils.models.VaccineDay
import com.drzymalski.covidinfo.config.ConfigurationManager
import com.drzymalski.covidinfo.config.CountryConfig
import com.drzymalski.covidinfo.interfaces.DataInitializer
import com.github.aachartmodel.aainfographics.aachartcreator.*
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAOptions
import com.github.aachartmodel.aainfographics.aatools.AAGradientColor
import com.drzymalski.covidinfo.config.backColor
import com.drzymalski.covidinfo.config.fontColor
import com.drzymalski.covidinfo.dataUtils.DateConverter
import com.drzymalski.covidinfo.dataUtils.VaccineStats

class VaccineInitializer: DataInitializer {

    private var stats = VaccineStats()
    var compareDates = mutableListOf<String>()
    var compareStats = mutableListOf<VaccineStats>()

    val csvManager = CSVManager()
    override val config: ConfigurationManager = ConfigurationManager()


    @ExperimentalStdlibApi
    fun loadScreenResources() = try {
        csvManager.loadVaccinationData()
        stats.country = config.config.selectedVaccine

        val filterDate = config.config.getDateFromVaccine()

        val statsPl = csvManager.vaccinationData
                .filter { filterDates(it, filterDate, config.config.selectedVaccine) }.toMutableList()
        stats.calculateStats(statsPl)

        compareStats.clear()
        config.config.vaccinationCountriesToCompare.forEach { countryConfig ->
            val newStats = VaccineStats()
            newStats.country = countryConfig
            val statsComp = csvManager.vaccinationData
                    .filter { filterDates(it, filterDate, countryConfig) }.toMutableList()
            newStats.calculateStats(statsComp)
            compareStats.plusAssign(newStats)
        }
        compareDates.clear()
        compareStats.forEach {
            if (compareDates.count()<it.datesList.count())
                compareDates = it.datesList
        }
        normalizeLengths()
    }catch (ex:Exception){
        println(ex.message) //need to see the errors xd
    }

    private fun normalizeLengths(){
        try {
            val topCnt = compareStats.find { it.datesList.size == compareDates.count()}
            if (topCnt!=null){
                compareStats.forEach { countryStat ->
                    if (countryStat.datesList.size<compareDates.count()){
                        val diffNew: Int = topCnt.datesList.size-countryStat.vaccineDaily.size
                        val diffTotal: Int = topCnt.datesList.size-countryStat.vaccineDoses.size

                        countryStat.datesList = topCnt.datesList

                        val newFirst = countryStat.vaccineDaily.first()
                        val totalFirst = countryStat.vaccineDoses.first()

                        for (i in 1..diffNew) {countryStat.vaccineDaily.add(0, newFirst)}
                        for (i in 1..diffTotal) {countryStat.vaccineDoses.add(0,totalFirst)}
                    }
                }
            }
        }catch (ex:Exception){
            println(ex.message) //need to see the errors xd
        }
    }

    private fun filterDates(vaccineDay: VaccineDay, filterDate: String, countryConfig: CountryConfig): Boolean =
            vaccineDay.iso_code == countryConfig.code && vaccineDay.date.toString() >= filterDate

    fun configureVaccineBarChart(): AAOptions {
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
                        .name("Zaszczepieni")
                        .lineWidth(4f)
                        .data(stats.vaccineDoses.toTypedArray())
                    .color(AAGradientColor.BerrySmoothie)
                )
            )
        return getChartOptions(aaChartModel)
    }

    fun configureVaccinePercentageBarChart(): AAOptions {

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
                                        .name("Procent zaszczepionych")
                                        .lineWidth(4f)
                                        .data(stats.vaccineDaily.toTypedArray())
                                        .color(AAGradientColor.BerrySmoothie)
                        )
                )
        return getChartOptions(aaChartModel)
    }

    fun configureNewVaccinationsBarChart(): AAOptions {

        val aaChartModel = AAChartModel()
                .chartType(AAChartType.Column)
                .title("")
                .yAxisTitle("")
                .zoomType(AAChartZoomType.X)
                .categories(stats.datesList.toTypedArray())
                .backgroundColor(backColor)
                .axesTextColor(fontColor)
                .series(
                        arrayOf(
                                AASeriesElement()
                                        .name("Ilość szczepień")
                                        .lineWidth(4f)
                                        .data(stats.vaccineDaily.toTypedArray())
                                        .color(AAGradientColor.BerrySmoothie)
                        )
                )
        return getChartOptions(aaChartModel)
    }

    fun configureCompareVaccinesChart(): AAOptions {
        val aaChartModel = AAChartModel()
                .chartType(AAChartType.Spline)
                .title("")
                .yAxisTitle("")
                .zoomType(AAChartZoomType.X)
                .markerSymbolStyle(AAChartSymbolStyleType.InnerBlank)
                .markerRadius(0f)
                .backgroundColor(backColor)
                .axesTextColor(fontColor)
                .categories(compareDates.toTypedArray())
                .series(
                        compareStats.map { stats ->
                            AASeriesElement()
                                    .name(stats.country.name)
                                    .lineWidth(3f)
                                    .color(stats.country.color)
                                    .data(stats.vaccineDoses.toTypedArray())
                        }.toTypedArray()
                )

        return getChartOptions(aaChartModel)
    }

    fun configureCompareVaccinesDailyChart(): AAOptions {
        val aaChartModel = AAChartModel()
                .chartType(AAChartType.Spline)
                .title("")
                .yAxisTitle("")
                .zoomType(AAChartZoomType.X)
                .markerSymbolStyle(AAChartSymbolStyleType.InnerBlank)
                .markerRadius(0f)
                .backgroundColor(backColor)
                .axesTextColor(fontColor)
                .categories(compareDates.toTypedArray())
                .series(
                        compareStats.map { stats ->
                            AASeriesElement()
                                    .name(stats.country.name)
                                    .lineWidth(3f)
                                    .color(stats.country.color)
                                    .data(stats.vaccineDaily.toTypedArray())
                        }.toTypedArray()
                )

        return getChartOptions(aaChartModel)
    }

}




