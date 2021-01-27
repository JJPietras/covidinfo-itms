package com.drzymalski.covidinfo.apiUtils

import com.drzymalski.covidinfo.apiUtils.models.PolandData
import com.drzymalski.covidinfo.apiUtils.models.VaccineDay
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.vhl.blackmo.grass.dsl.grass
import java.net.HttpURLConnection
import java.net.URL

class CSVManager {
    var vaccinationData = mutableListOf<VaccineDay>()

    @ExperimentalStdlibApi
    fun loadVaccinationData(){
        val url =
            URL("https://raw.githubusercontent.com/owid/covid-19-data/master/public/data/vaccinations/vaccinations.csv")
        val urlConnection = url.openConnection() as HttpURLConnection

        var text = ""

        try {
            text = urlConnection.inputStream.bufferedReader().readText()
        } catch (ex: Exception) {
            println(ex.message)
        } finally {
            urlConnection.disconnect()
        }
        try {
            val csvContents = csvReader().readAllWithHeader(data = text)

            val classes = grass<VaccineDay>().harvest(csvContents)
            vaccinationData = classes.toMutableList()
        } catch (ex: Exception) {
            println(ex)
        }

    }

    companion object{
        @OptIn(ExperimentalStdlibApi::class)
        fun loadPolandData(): MutableList<PolandData>{
            val url =  URL("https://arcgis.com/sharing/rest/content/items/829ec9ff36bc45a88e1245a82fff4ee0/data" )
            val urlConnection = url.openConnection() as HttpURLConnection

            var text = ""

            try {
                text = urlConnection.inputStream.bufferedReader().readText()
            } catch (ex: Exception) {
                println(ex.message)
            } finally {
                urlConnection.disconnect()
            }
            try {
                val csvContents = csvReader().readAllWithHeader(data = text.replace(";", ","))

                val classes = grass<PolandData>().harvest(csvContents)
                return classes.toMutableList()
            } catch (ex: Exception) {
                println(ex)
            }
            return mutableListOf()
        }
    }


}