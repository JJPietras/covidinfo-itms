package com.drzymalski.covidinfo.ui.todayIllness

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.drzymalski.covidinfo.R
import com.drzymalski.covidinfo.apiUtils.models.Country
import com.drzymalski.covidinfo.plottingUtils.TodayIllnessInitializer
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import kotlin.properties.Delegates

class TodayIllnessFragment : Fragment() {

    private lateinit var todayIllnessViewModel: TodayIllnessViewModel

    private var todayIllnessInitializer: TodayIllnessInitializer = TodayIllnessInitializer()
    private var selectedDay by Delegates.notNull<Int>()

    private lateinit var aaChartViewNewCases: AAChartView
    private lateinit var aaChartViewTotalCases: AAChartView
    private lateinit var aaChartActiveTotalCases: AAChartView
    private lateinit var aaChartRecovered: AAChartView
    private lateinit var aaChartDeadlyCases: AAChartView

    private lateinit var tDate: TextView

    private lateinit var tInfections: TextView
    private lateinit var tDied: TextView
    private lateinit var tRecovered: TextView

    private lateinit var tIncreaseNum: TextView
    private lateinit var tIncreasePercent: TextView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        todayIllnessViewModel =
                ViewModelProviders.of(this).get(TodayIllnessViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_today, container, false)
        todayIllnessViewModel.text.observe(viewLifecycleOwner, Observer { })

        tDate = root.findViewById(R.id.date)
        tInfections = root.findViewById(R.id.infections)
        tDied = root.findViewById(R.id.died)
        tRecovered = root.findViewById(R.id.recovered)

        tIncreaseNum = root.findViewById(R.id.increaseNum)
        tIncreasePercent = root.findViewById(R.id.increasePercent)

        todayIllnessInitializer.data.loadMainScreenResouces("2020-10-01")

        aaChartViewNewCases = root.findViewById<AAChartView>(R.id.aaChartViewNewCases)
        aaChartViewNewCases.aa_drawChartWithChartOptions(todayIllnessInitializer.configureNewCasesBarChart())

        aaChartViewTotalCases = root.findViewById<AAChartView>(R.id.aaChartViewTotalCases)
        aaChartViewTotalCases.aa_drawChartWithChartOptions(todayIllnessInitializer.configureTotalCasesBarChart())

        aaChartActiveTotalCases = root.findViewById<AAChartView>(R.id.aaChartViewActiveCases)
        aaChartActiveTotalCases.aa_drawChartWithChartOptions(todayIllnessInitializer.configureAvtiveBarChart())

        aaChartRecovered = root.findViewById<AAChartView>(R.id.aaChartViewRecovered)
        aaChartRecovered.aa_drawChartWithChartOptions(todayIllnessInitializer.configureRecoveredBarChart())

        aaChartDeadlyCases = root.findViewById<AAChartView>(R.id.aaChartViewDied)
        aaChartDeadlyCases.aa_drawChartWithChartOptions(todayIllnessInitializer.configureDeathsBarChart())

        selectedDay = todayIllnessInitializer.data.datesFullList.lastIndex -1

        if (selectedDay >0){

            tDate.text = todayIllnessInitializer.data.datesFullList[selectedDay + 1]
            val new =  todayIllnessInitializer.data.newCasesList[selectedDay]
            tInfections.text = new.toString()
            tDied.text = todayIllnessInitializer.data.newDeathsList[selectedDay].toString()
            tRecovered.text = todayIllnessInitializer.data.newRecoveredList[selectedDay].toString()

            val yesterdayNew =  todayIllnessInitializer.data.newCasesList[selectedDay-1]
            val nInc  =  (new - yesterdayNew)
            val pInc: Float =  ((new.toFloat() / yesterdayNew.toFloat()) - 1f) * 100f

            if (nInc<0){
                tIncreaseNum.setTextColor(Color.parseColor("#388E3C"))
                tIncreasePercent.setTextColor(Color.parseColor("#388E3C"))
            }
            else{
                tIncreaseNum.setTextColor(Color.parseColor("#C2185B"))
                tIncreasePercent.setTextColor(Color.parseColor("#C2185B"))
            }
            tIncreaseNum.text = nInc.toString()
            tIncreasePercent.text = "${"%.1f".format(pInc)}%" // I know, I know
        }
        return root
    }
}