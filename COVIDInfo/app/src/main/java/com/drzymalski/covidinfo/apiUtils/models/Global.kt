package com.drzymalski.covidinfo.apiUtils.models;

data class Global(val NewConfirmed: Int,val TotalConfirmed: Int, val NewDeaths: Int,
                  val TotalDeaths: Int, val NewRecovered: Int, val TotalRecovered: Int) {

}