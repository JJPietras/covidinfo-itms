package com.drzymalski.covidinfo.config

import com.drzymalski.covidinfo.dataUtils.DateConverter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class Config {
    var countries = mutableListOf<CountryConfig>()
    var countriesToCompare = mutableListOf<CountryConfig>()
    var selectedCountry = CountryConfig()
    //var dateFrom = "2020-10-01" //will be replaced by the amount of days
    var daysBackCompare:Long = 30
    var daysBackToday:Long = 30

    fun getDateFromCompare(): String{
        val date: LocalDateTime  = LocalDateTime.now().minusDays(daysBackCompare)
        val out: Date = Date.from(date.atZone(ZoneId.systemDefault()).toInstant())
        return DateConverter.formatDateFull(out)
    }

    fun getDateFromMain(): String{
        val date: LocalDateTime  = LocalDateTime.now().minusDays(daysBackToday)
        val out: Date = Date.from(date.atZone(ZoneId.systemDefault()).toInstant())
        return DateConverter.formatDateFull(out)
    }
}