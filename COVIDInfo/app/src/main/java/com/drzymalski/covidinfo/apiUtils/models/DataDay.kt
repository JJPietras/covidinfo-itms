package com.drzymalski.covidinfo.apiUtils.models

import java.util.*

data class DataDay(val iso3166_1: String, val date_stamp: Date, val cnt_confirmed: Int,
                   val cnt_death: Int, val cnt_recovered: Int, val cnt_active: Int){}
