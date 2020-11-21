package com.drzymalski.covidinfo.ui.suspicion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SuspicionViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Illness Suspicion Fragment"
    }
    val text: LiveData<String> = _text
}