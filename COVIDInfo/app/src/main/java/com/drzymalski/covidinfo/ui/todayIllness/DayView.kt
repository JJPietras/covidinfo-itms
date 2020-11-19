package com.drzymalski.covidinfo.ui.todayIllness

import androidx.lifecycle.MutableLiveData

class DayView {
    var date: MutableLiveData<String> = MutableLiveData("...")
    var confirmed: MutableLiveData<Int> = MutableLiveData(0)
    var deaths: MutableLiveData<Int> = MutableLiveData(0)
    var recovered: MutableLiveData<Int> = MutableLiveData(0)

    var increaseCount: MutableLiveData<Int> = MutableLiveData(0)
    var increasePercent: MutableLiveData<Float> = MutableLiveData(0f)

}