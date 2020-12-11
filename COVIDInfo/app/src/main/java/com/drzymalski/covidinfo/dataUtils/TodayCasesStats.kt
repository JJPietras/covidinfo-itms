package com.drzymalski.covidinfo.dataUtils

import com.drzymalski.covidinfo.apiUtils.models.DataProvider
import com.drzymalski.covidinfo.apiUtils.models.old.CovidDay

class TodayCasesStats{
    val totalCasesList = mutableListOf<Int>()

    val newDeathsList = mutableListOf<Int>()
    val newRecoveredList = mutableListOf<Int>()

    val activeCasesList = mutableListOf<Int>()

    val newCasesList = mutableListOf<Int>()
    val newCasesWeeklyList = mutableListOf<Float>()

    val datesList = mutableListOf<String>()
    val datesFullList = mutableListOf<String>()


    private fun clearData(){
        totalCasesList.clear()

        newDeathsList.clear()
        newRecoveredList.clear()

        activeCasesList.clear()

        newCasesList.clear()
        newCasesWeeklyList.clear()

        datesList.clear()
        datesFullList.clear()
    }

    private fun getWeeklyAverage(){
        newCasesWeeklyList.clear()
        for (i in 0 until newCasesList.size){
            val from = if (i-3<0) 0 else i-3
            val to = if (i+3>=newCasesList.size-1) newCasesList.size-1 else i+3
            var sum = 0f
            val count = to - from + 1f
            for (j in from..to) sum += newCasesList[j].toFloat()
            newCasesWeeklyList += (sum/count)
        }
    }

    fun calculateStats(covidData: DataProvider){
        clearData()
        var lastCases = 0
        var lastDeaths = 0
        var lastRecovered = 0

        covidData.dataProvider.forEach { casesOnDay ->
            run {
                totalCasesList += casesOnDay.cnt_confirmed

                activeCasesList += casesOnDay.cnt_active
                @Suppress("DEPRECATION")
                datesList += DateConverter.formatDateShort(casesOnDay.date_stamp)
                datesFullList += DateConverter.formatDateFull(casesOnDay.date_stamp)

                if (lastDeaths==0) {lastDeaths = casesOnDay.cnt_death}
                else {
                    newDeathsList += (casesOnDay.cnt_death - lastDeaths)
                    lastDeaths = casesOnDay.cnt_death
                }

                if (lastRecovered==0) {lastRecovered = casesOnDay.cnt_recovered}
                else {
                    newRecoveredList += (casesOnDay.cnt_recovered - lastRecovered)
                    lastRecovered = casesOnDay.cnt_recovered
                }

                if (lastCases==0) {lastCases = casesOnDay.cnt_confirmed}
                else {
                    newCasesList += (casesOnDay.cnt_confirmed - lastCases)
                    lastCases = casesOnDay.cnt_confirmed
                }
            }
        }
        getWeeklyAverage()
    }
}