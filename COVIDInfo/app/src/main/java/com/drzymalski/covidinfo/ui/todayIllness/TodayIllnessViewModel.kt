package com.drzymalski.covidinfo.ui.todayIllness

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel



class TodayIllnessViewModel : ViewModel() {
    var selectedDayData: DayView = DayView()



    val date: LiveData<String> = selectedDayData.date
    val confirmed: LiveData<Int> = selectedDayData.confirmed

    val died: LiveData<Int> = selectedDayData.deaths
    val recovered: LiveData<Int> = selectedDayData.recovered

    var pIncreaseCount: LiveData<Float> = selectedDayData.increasePercent
    var nIncreaseCount: LiveData<Int> = selectedDayData.increaseCount

    fun calcIncrease(conf: Int, confYesterday: Int){
        selectedDayData.increasePercent.value = ((conf.toFloat() / confYesterday.toFloat()) - 1f) * 100f
        selectedDayData.increaseCount.value  = conf - confYesterday
    }
}