package com.drzymalski.covidinfo.dataUtils

import com.drzymalski.covidinfo.apiUtils.models.CovidDay

class TodayCasesStats {
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

    fun calculateStats(covidData: List<CovidDay>){
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
                datesFullList += DateConverter.formatDateFull(casesOnDay.Date)

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
    }
}