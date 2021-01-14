package com.drzymalski.covidinfo.ui.vaccine

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VaccineViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Vaccine Fragment"
    }
    val text: LiveData<String> = _text
}