package com.drzymalski.covidinfo.ui.compare

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CompareViewModel : ViewModel() {
    var dateLive: MutableLiveData<String> = MutableLiveData("Wczytywanie")

    val date: LiveData<String> = dateLive
}