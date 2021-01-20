package com.drzymalski.covidinfo.dataUtils

import com.drzymalski.covidinfo.apiUtils.models.DataProvider

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
        var lastCases = -1
        var lastDeaths = -1
        var lastRecovered = -1

        covidData.dataProvider.forEach { casesOnDay ->
            run {
                totalCasesList += casesOnDay.cnt_confirmed
                activeCasesList += casesOnDay.cnt_active

                datesList += DateConverter.formatDateShort(casesOnDay.date_stamp)
                datesFullList += DateConverter.formatDateFull(casesOnDay.date_stamp)

                if (lastDeaths==-1) {lastDeaths = casesOnDay.cnt_death}
                else {
                    var temp = casesOnDay.cnt_death - lastDeaths
                    if (temp < 0) temp = 0
                    newDeathsList += temp
                    lastDeaths = casesOnDay.cnt_death
                }

                if (lastRecovered==-1) {lastRecovered = casesOnDay.cnt_recovered}
                else {
                    var temp = casesOnDay.cnt_recovered - lastRecovered
                    if (temp < 0) temp = 0
                    newRecoveredList += temp
                    lastRecovered = casesOnDay.cnt_recovered
                }

                if (lastCases==-1) {lastCases = casesOnDay.cnt_confirmed}
                else {
                    var temp = casesOnDay.cnt_confirmed - lastCases
                    if (temp < 0) temp = 0
                    newCasesList += temp
                    lastCases = casesOnDay.cnt_confirmed
                }
            }
        }
        getWeeklyAverage()
    }
}