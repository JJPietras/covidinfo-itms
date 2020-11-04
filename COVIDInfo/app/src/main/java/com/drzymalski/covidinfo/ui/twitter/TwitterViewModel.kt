package com.drzymalski.covidinfo.ui.twitter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TwitterViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Twitter Fragment"
    }
    val text: LiveData<String> = _text
}