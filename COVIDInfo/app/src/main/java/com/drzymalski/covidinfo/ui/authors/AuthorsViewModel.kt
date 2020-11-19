package com.drzymalski.covidinfo.ui.authors

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AuthorsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Hospitals Fragment"
    }
    val text: LiveData<String> = _text
}