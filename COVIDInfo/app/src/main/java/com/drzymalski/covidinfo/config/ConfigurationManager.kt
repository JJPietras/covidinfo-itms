package com.drzymalski.covidinfo.config

import com.neovisionaries.i18n.CountryCode
import io.paperdb.Paper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class ConfigurationManager {
    var config: Config = Config()

    init {
        try {
            loadConfig()
            GlobalScope.launch {
                when {
                    config.selectedVaccine.code != "" -> {
                        if (config.selectedVaccine.code.length == 2) {
                            config.selectedVaccine.code =
                                CountryCode.getByCode(config.selectedVaccine.code).alpha3
                            saveConfig()
                        }
                    }
                    else -> {
                        val cc = CountryCode.getByCode("PL").alpha3
                        config.selectedVaccine = CountryConfig().apply {
                            slug = "poland"
                            name = "Polska"
                            continent = "Europa"
                            color = "#6f79fc"
                            code = cc
                        }
                        saveConfig()
                    }
                }
            }
        } catch (ex: Exception){
            initializeAndSaveTheBasicConfig()
        }
        if (config.countries.size == 0) initializeAndSaveTheBasicConfig()
    }

    fun saveConfig(){
        Paper.book().write("config.json", config)
    }

    private fun loadConfig(){
        config = Paper.book().read("config.json")
    }

    private fun initializeAndSaveTheBasicConfig(){
        config.countries = mutableListOf(
            CountryConfig().apply {
                slug = "poland"
                name = "Polska"
                continent = "Europa"
                color = "#6f79fc"
                code = "PL"
            },
            CountryConfig().apply {
                slug = "germany"
                name = "Niemcy"
                continent = "Europa"
                color = "#F44336"
                code = "DE"
            },
            CountryConfig().apply {
                slug = "italy"
                name = "Włochy"
                continent = "Europa"
                color = "#009688"
                code = "IT"
            },
            CountryConfig().apply {
                slug = "united-states"
                name = "USA"
                continent = "Ameryka Północna"
                color = "#FFC107"
                code = "US"
            })

        config.selectedCountry = CountryConfig().apply {
            slug = "poland"
            name = "Polska"
            continent = "Europa"
            color = "#6f79fc"
            code = "PL"
        }

        config.countriesToCompare = mutableListOf(
            CountryConfig().apply {
                slug = "poland"
                name = "Polska"
                continent = "Europa"
                color = "#6f79fc"
                code = "PL"
            },
            CountryConfig().apply {
                slug = "germany"
                name = "Niemcy"
                continent = "Europa"
                color = "#F44336"
                code = "DE"
            },
            CountryConfig().apply {
                slug = "italy"
                name = "Włochy"
                continent = "Europa"
                color = "#009688"
                code = "IT"
            })

        config.selectedVaccine = CountryConfig().apply {
            slug = "poland"
            name = "Polska"
            continent = "Europa"
            color = "#6f79fc"
            code = "POL"
        }
        saveConfig()
    }

}