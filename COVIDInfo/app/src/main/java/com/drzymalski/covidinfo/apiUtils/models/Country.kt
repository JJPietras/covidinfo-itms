package com.drzymalski.covidinfo.apiUtils.models

import java.util.*

data class Country(val Country: String, val CountryCode: String, val Slug: String,
                   val NewConfirmed: Int, val TotalConfirmed: Int, val NewDeaths: Int,
                   val TotalDeaths: Int, val NewRecovered: Int, val TotalRecovered: Int,
                   val date: Date)