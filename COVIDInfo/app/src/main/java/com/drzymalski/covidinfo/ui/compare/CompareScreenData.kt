package com.drzymalski.covidinfo.ui.compare

import com.drzymalski.covidinfo.apiUtils.ApiManager
import com.drzymalski.covidinfo.apiUtils.models.SummaryData
import com.drzymalski.covidinfo.config.ConfigurationManager
import com.drzymalski.covidinfo.dataUtils.CompareCasesStats
import com.drzymalski.covidinfo.dataUtils.DateConverter

class CompareScreenData {

    lateinit var summaryData: SummaryData

    val stats = mutableListOf<CompareCasesStats>()

    val config: ConfigurationManager = ConfigurationManager()

    fun loadScreenResources(){
        try {
            summaryData = ApiManager.getSummaryFromApi()
            summaryData.Countries = summaryData.Countries.filter{config.config.countries
                .map{ countryConfig -> countryConfig.slug }.contains(it.Slug)}
        }catch (ex:Exception){
            println(ex.message) //need to see the errors xd
        }
    }
}