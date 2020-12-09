package com.drzymalski.covidinfo.dataUtils

import android.media.audiofx.DynamicsProcessing
import com.drzymalski.covidinfo.apiUtils.models.CovidDay
import com.drzymalski.covidinfo.config.Config
import com.drzymalski.covidinfo.config.CountryConfig

class CompareCasesStats {
    val totalCasesList = mutableListOf<Int>()

    private val newDeathsList = mutableListOf<Int>()
    private val newRecoveredList = mutableListOf<Int>()
    private val newCasesList = mutableListOf<Int>()

    val activeCasesList = mutableListOf<Int>()

    val newCasesWeeklyList = mutableListOf<Float>()
    val newDeathsWeeklyList = mutableListOf<Float>()
    val newRecoveredWeeklyList = mutableListOf<Float>()

    val datesList = mutableListOf<String>()
    //val datesFullList = mutableListOf<String>()

    var country: CountryConfig = CountryConfig()

    fun clearData(){
        totalCasesList.clear()

        newDeathsList.clear()
        newRecoveredList.clear()
        newCasesList.clear()

        activeCasesList.clear()

        newCasesWeeklyList.clear()
        newDeathsWeeklyList.clear()
        newRecoveredWeeklyList.clear()

        datesList.clear()
        //datesFullList.clear()
    }

    private fun getWeeklyAverage(){
        newCasesWeeklyList.clear()
        newDeathsWeeklyList.clear()
        newRecoveredWeeklyList.clear()

        for (i in 0 until newCasesList.size){
            val from = if (i-3<0) 0 else i-3
            val to = if (i+3>=newCasesList.size-1) newCasesList.size-1 else i+3
            var sumNew = 0f
            var sumDeaths = 0f
            var sumRecovered = 0f
            val count = to - from + 1f
            for (j in from..to) {
                sumNew += newCasesList[j].toFloat()
                sumDeaths += newDeathsList[j].toFloat()
                sumRecovered += newRecoveredList[j].toFloat()
            }
            newCasesWeeklyList += (sumNew/count)
            newDeathsWeeklyList += (sumDeaths/count)
            newRecoveredWeeklyList += (sumRecovered/count)
        }

        newCasesList.clear()
        newDeathsList.clear()
        newRecoveredList.clear()
    }

    fun calculateStats(covidData: List<CovidDay>){
        try {
            clearData()
            var lastCases = 0
            var lastDeaths = 0
            var lastRecovered = 0

            covidData.forEach { casesOnDay ->
                run {
                    totalCasesList += casesOnDay.Confirmed

                    activeCasesList += casesOnDay.Active
                    @Suppress("DEPRECATION")
                    datesList += DateConverter.formatDateShort(casesOnDay.Date)
                    //datesFullList += DateConverter.formatDateFull(casesOnDay.Date)

                    if (lastDeaths==0) {lastDeaths = casesOnDay.Deaths}
                    else {
                        newDeathsList += (casesOnDay.Deaths - lastDeaths)
                        lastDeaths = casesOnDay.Deaths
                    }

                    if (lastRecovered==0) {lastRecovered = casesOnDay.Recovered}
                    else {
                        newRecoveredList += (casesOnDay.Recovered - lastRecovered)
                        lastRecovered = casesOnDay.Recovered
                    }

                    if (lastCases==0) {lastCases = casesOnDay.Confirmed}
                    else {
                        newCasesList += (casesOnDay.Confirmed - lastCases)
                        lastCases = casesOnDay.Confirmed
                    }
                }
            }
            getWeeklyAverage()
        }catch (ex:Exception){
            println(ex.message) //need to see the errors xd
        }
    }
}