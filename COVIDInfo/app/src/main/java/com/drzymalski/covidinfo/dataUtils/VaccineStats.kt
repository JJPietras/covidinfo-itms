package com.drzymalski.covidinfo.dataUtils

import com.drzymalski.covidinfo.apiUtils.CSVManager
import com.drzymalski.covidinfo.apiUtils.models.VaccineDay
import com.drzymalski.covidinfo.config.CountryConfig

class VaccineStats {

    var datesList =  mutableListOf<String>()
    var vaccineDoses =  mutableListOf<Int>()
    var vaccineDaily =  mutableListOf<Int>()

    var country: CountryConfig = CountryConfig()

    var totalVaccinated = 0
    var vaccinationPercentage = 0f

    private fun clearData(){
        datesList.clear()
        vaccineDoses.clear()
        vaccineDaily.clear()
    }

    fun calculateStats(stats: MutableList<VaccineDay>){
        clearData()
        var lastGiven = 0
        var lastDaily = 0
        var lastDate = ""
        stats.forEach { vaccineOnDay ->
            if (vaccineOnDay.date!=null) {
                datesList.plusAssign(vaccineOnDay.date)
                lastDate = vaccineOnDay.date
            }
            else datesList.plusAssign(lastDate)

            var temp = vaccineOnDay.people_vaccinated
            if (temp!=null ){
                if (temp <= 0) temp = lastGiven
                vaccineDoses.plusAssign(temp)
                lastGiven = temp
            }
            else vaccineDoses.plusAssign(lastGiven)

            var temp2 = vaccineOnDay.daily_vaccinations
            if (temp2!=null ){
                if (temp2 <= 0) temp2 = lastDaily
                vaccineDaily.plusAssign(temp2)
                lastDaily = temp2
            }
            else vaccineDaily.plusAssign(lastGiven)

            if (vaccineOnDay.people_vaccinated_per_hundred!=null)
                vaccinationPercentage = vaccineOnDay.people_vaccinated_per_hundred
        }
        totalVaccinated = lastGiven

    }
}