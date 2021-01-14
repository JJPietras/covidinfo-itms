package com.drzymalski.covidinfo.config

import com.drzymalski.covidinfo.dataUtils.DateConverter
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

const val backColor: String = "#3A4098"
const val fontColor: String = "#CCCCCC"
class Config {
    var countries = mutableListOf<CountryConfig>()
    var countriesToCompare = mutableListOf<CountryConfig>()
    var selectedCountry = CountryConfig()
    var daysBackCompare:Long = 30
    var daysBackToday:Long = 30

    var selectedVaccine = CountryConfig()

    fun getDateFromMain(): String{
        val date: LocalDateTime  = LocalDateTime.now().minusDays(daysBackToday + 1) // +1 to account for 1 day which cannot be calculated
        val out: Date = Date.from(date.atZone(ZoneId.systemDefault()).toInstant())
        return DateConverter.formatDateFull(out)
    }

    fun getDateFromCompare(): String{
        val date: LocalDateTime  = LocalDateTime.now().minusDays(daysBackCompare + 1) // +1 to account for 1 day which cannot be calculated
        val out: Date = Date.from(date.atZone(ZoneId.systemDefault()).toInstant())
        return DateConverter.formatDateFull(out)
    }

    fun getCompareRequest(): String{
        var str = ""
        countriesToCompare.forEach { country ->
            str += if (str=="") "iso3166_1=" + country.code
            else " or iso3166_1=" + country.code
        }
        return str
    }


}