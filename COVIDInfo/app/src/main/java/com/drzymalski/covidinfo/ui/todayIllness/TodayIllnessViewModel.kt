package com.drzymalski.covidinfo.ui.todayIllness

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TodayIllnessViewModel : ViewModel() {

    var dateLive: MutableLiveData<String> = MutableLiveData("...")
    var confirmedLive: MutableLiveData<Int> = MutableLiveData(0)
    var deathsLive: MutableLiveData<Int> = MutableLiveData(0)
    var recoveredLive: MutableLiveData<Int> = MutableLiveData(0)

    var increaseCountLive: MutableLiveData<Int> = MutableLiveData(0)
    var increasePercentLive: MutableLiveData<Float> = MutableLiveData(0f)

    val date: LiveData<String> = dateLive
    val confirmed: LiveData<Int> = confirmedLive

    val died: LiveData<Int> = deathsLive
    val recovered: LiveData<Int> = recoveredLive

    var pIncreaseCount: LiveData<Float> = increasePercentLive
    var nIncreaseCount: LiveData<Int> = increaseCountLive

    fun calcIncrease(conf: Int, confYesterday: Int){
        increasePercentLive.postValue(((conf.toFloat() / confYesterday.toFloat()) - 1f) * 100f)
        increaseCountLive.postValue(conf - confYesterday)
    }
}