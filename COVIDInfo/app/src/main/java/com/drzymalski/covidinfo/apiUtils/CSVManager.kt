package com.drzymalski.covidinfo.apiUtils

import com.drzymalski.covidinfo.apiUtils.models.PolandData
import com.drzymalski.covidinfo.apiUtils.models.VaccineDay
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.StringReader
import java.net.HttpURLConnection
import java.net.URL

class CSVManager {
    var vaccinationData = mutableListOf<VaccineDay>()

    private val csvMapper = CsvMapper().apply {
        registerModule(KotlinModule())
        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    }

    private inline fun <reified T> readCsv(contents: String): MutableList<T> {
            StringReader(contents).use { reader ->
            return csvMapper
                .readerFor(T::class.java)
                .with(CsvSchema.emptySchema().withHeader())
                .readValues<T>(reader)
                .readAll()
                .toMutableList()
        }
    }

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
            vaccinationData = readCsv(text)
        } catch (ex: Exception) {
            println(ex)
        }

    }

    fun loadPolandData(): MutableList<PolandData>{
        val url =  URL("https://www.arcgis.com/sharing/rest/content/items/153a138859bb4c418156642b5b74925b/data")
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
            return readCsv(text.replace(";", ","))
        } catch (ex: Exception) {
            println(ex)
        }
        return mutableListOf()
    }

}