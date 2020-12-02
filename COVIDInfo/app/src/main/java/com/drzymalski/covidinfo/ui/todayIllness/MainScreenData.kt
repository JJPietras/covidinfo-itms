package com.drzymalski.covidinfo.ui.todayIllness

import com.drzymalski.covidinfo.apiUtils.ApiManager
import com.drzymalski.covidinfo.apiUtils.models.CovidDay
import com.drzymalski.covidinfo.apiUtils.models.SummaryData
import com.drzymalski.covidinfo.dataUtils.TodayCasesStats
import com.drzymalski.covidinfo.config.ConfigurationManager
import com.drzymalski.covidinfo.dataUtils.DateConverter

class MainScreenData {

    private var covidData: List<CovidDay>  = mutableListOf()
    lateinit var summaryData: SummaryData

    val stats = TodayCasesStats()

    val config: ConfigurationManager = ConfigurationManager()

    fun loadMainScreenResouces(){
        try {
            summaryData = ApiManager.getSummaryFromApi()
            summaryData.Countries = summaryData.Countries.filter{config.config.countries
                .map{ countryConfig -> countryConfig.slug }.contains(it.Slug)}
        }catch (ex:Exception){
            println(ex.message) //need to see the errors xd
        }
        covidData = ApiManager.getCovidDataFromApi(config.config.dateFrom,
            DateConverter.formatDateFull(summaryData.Date), config.config.selectedCountry.slug )
        stats.calculateStats(covidData)
    }
}