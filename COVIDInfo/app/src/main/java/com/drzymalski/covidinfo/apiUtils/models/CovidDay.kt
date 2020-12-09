package com.drzymalski.covidinfo.apiUtils.models

import java.util.*

data class CovidDay(val Date: Date, val Confirmed: Int, val Deaths: Int,
                    val Recovered: Int, val Active: Int)