package com.drzymalski.covidinfo.apiUtils.models
import java.util.*

data class VaccineDay(val location: String, val iso_code: String?, val date: String?,
                      val total_vaccinations: Long?, val people_vaccinated: Long?,
                      val people_fully_vaccinated: Long?, val daily_vaccinations_raw: Long?,
                      val daily_vaccinations: Long?, val total_vaccinations_per_hundred: Float?,
                      val people_vaccinated_per_hundred: Float?,
                      val people_fully_vaccinated_per_hundred: Float?,
                      val daily_vaccinations_per_million: Float?)
