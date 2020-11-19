package com.drzymalski.covidinfo.ui.selector

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SelectorViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Selector Fragment"
    }
    val text: LiveData<String> = _text
}