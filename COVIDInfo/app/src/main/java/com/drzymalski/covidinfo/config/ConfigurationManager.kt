package com.drzymalski.covidinfo.config

import io.paperdb.Paper

class ConfigurationManager {
    var config: Config = Config()

    init {
        try {
            loadConfig()
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
        saveConfig()
    }

}