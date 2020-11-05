package com.drzymalski.covidinfo.ui.todayIllness

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.drzymalski.covidinfo.R
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement



class TodayIllnessViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Today Illness Fragment"
    }
    val text: LiveData<String> = _text

}