package com.drzymalski.covidinfo.apiUtils.models

import com.drzymalski.covidinfo.apiUtils.models.Country
import com.drzymalski.covidinfo.apiUtils.models.Global
import java.util.*

data class SummaryData(val Global: Global, var Countries: List<Country>, var Date: Date)