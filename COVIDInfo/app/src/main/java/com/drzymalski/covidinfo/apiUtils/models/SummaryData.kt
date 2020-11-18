package com.drzymalski.covidinfo.apiUtils.models

import java.util.*

data class SummaryData(val Global: Global, var Countries: List<Country>, var Date: Date)  {

}