package com.drzymalski.covidinfo.ui.vaccine


import android.content.Context
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import com.drzymalski.covidinfo.config.CountryConfig
import com.drzymalski.covidinfo.data.Countries
import com.drzymalski.covidinfo.interfaces.FragmentSettings
import com.drzymalski.covidinfo.ui.settings.SettingsView
import com.neovisionaries.i18n.CountryCode

open class VaccineSettingsView(
        context: Context,
        rootLayout: LinearLayout,
        countries: MutableList<CountryConfig>,
        daysBack: Long,
        fragmentSettings: FragmentSettings,
        vaccineCountries: MutableList<String>)
    : SettingsView(context, rootLayout, countries, daysBack, fragmentSettings) {

    var vaccineCountries = mutableListOf<String>()

    init {
        this.vaccineCountries = vaccineCountries
    }

    override fun configurePickers()  {
        daysPicker.minValue = 7
        daysPicker.maxValue = 365
        daysPicker.value = daysBackNew

        val searchMethod = ArrayAdapter(context,
                android.R.layout.simple_spinner_item,
                Countries.clist.filter { CountryCode.getByCode(it.code).alpha3 in vaccineCountries }
                        .map { "${it.name} ( ${it.code} )" }.toTypedArray()
        )
        searchMethod.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = searchMethod

        vaccineInfo.visibility = VISIBLE
    }

}