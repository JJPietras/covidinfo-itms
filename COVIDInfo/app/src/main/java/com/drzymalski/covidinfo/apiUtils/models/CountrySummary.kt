package com.drzymalski.covidinfo.apiUtils.models

import java.util.*

data class CountrySummary(val iso3166_1: String, val cnt_confirmed: Int, val cnt_death: Int,
                          val cnt_recovered: Int, val cnt_active: Int)
