package com.drzymalski.covidinfo.plottingUtils

class Config {
    var countries = mutableListOf<CountryConfig>()
    var selectedCountry = CountryConfig()
    var dateFrom = "2020-10-01" //will be replaced by the amount of days
}