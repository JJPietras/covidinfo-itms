package com.drzymalski.covidinfo.interfaces

import com.drzymalski.covidinfo.config.CountryConfig

interface FragmentSettings {

    fun applySettings(countries: MutableList<CountryConfig>, daysBack: Long)
}