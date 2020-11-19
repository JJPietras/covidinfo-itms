package com.drzymalski.covidinfo.ui.todayIllness

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.drzymalski.covidinfo.R
import com.drzymalski.covidinfo.apiUtils.models.Country
import com.drzymalski.covidinfo.apiUtils.models.CovidDay
import com.drzymalski.covidinfo.plottingUtils.TodayIllnessInitializer
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import kotlinx.android.synthetic.main.nav_header_main.*
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

    private lateinit var prevDayBt: ImageButton
    private lateinit var nextDayBt: ImageButton

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        todayIllnessViewModel =
                ViewModelProviders.of(this).get(TodayIllnessViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_today, container, false)

        tDate = root.findViewById(R.id.date)
        tInfections = root.findViewById(R.id.infections)
        tDied = root.findViewById(R.id.died)
        tRecovered = root.findViewById(R.id.recovered)

        tIncreaseNum = root.findViewById(R.id.increaseNum)
        tIncreasePercent = root.findViewById(R.id.increasePercent)

        prevDayBt = root.findViewById(R.id.prevDayBt)
        prevDayBt.setOnClickListener {
            if (selectedDay > 1){
                selectedDay -= 1
                setData()
            }
            buttonVisibility()
        }

        nextDayBt = root.findViewById(R.id.nextDayBt)
        nextDayBt.setOnClickListener {
            if (selectedDay < todayIllnessInitializer.data.datesFullList.lastIndex){
                selectedDay += 1
                setData()
            }
            buttonVisibility()
        }

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

        todayIllnessViewModel.confirmed.observe(viewLifecycleOwner, Observer { tInfections.text = it.toString() })
        todayIllnessViewModel.died.observe(viewLifecycleOwner, Observer { tDied.text = it.toString() })
        todayIllnessViewModel.recovered.observe(viewLifecycleOwner, Observer { tRecovered.text = it.toString() })

        todayIllnessViewModel.date.observe(viewLifecycleOwner, Observer { tDate.text = it.toString() })

        todayIllnessViewModel.nIncreaseCount.observe(viewLifecycleOwner, Observer {
            tIncreaseNum.text =  "${if (it > 0) "+" else ""}${it}"
            tIncreaseNum.setTextColor(Color.parseColor( if (it < 0) "#388E3C" else "#C2185B" ))
        })
        todayIllnessViewModel.pIncreaseCount.observe(viewLifecycleOwner, Observer {
            tIncreasePercent.text = "${if (it > 0f) "+" else ""}${"%.1f".format(it)}%"
            tIncreasePercent.setTextColor(Color.parseColor( if (it < 0f) "#388E3C" else "#C2185B" ))
        })

        selectedDay = todayIllnessInitializer.data.datesFullList.lastIndex -1

        setData()
        buttonVisibility()
        return root
    }
    private fun buttonVisibility(){
        nextDayBt.visibility = if (selectedDay == todayIllnessInitializer.data.datesFullList.lastIndex-1) View.INVISIBLE else View.VISIBLE
        prevDayBt.visibility = if (selectedDay == 1) View.INVISIBLE else View.VISIBLE
    }

    private fun setData(){
        if (selectedDay > 0 && selectedDay < todayIllnessInitializer.data.datesFullList.lastIndex)  {
            todayIllnessViewModel.selectedDayData.date.value = todayIllnessInitializer.data.datesFullList[selectedDay + 1]

            todayIllnessViewModel.selectedDayData.confirmed.value = todayIllnessInitializer.data.newCasesList[selectedDay]
            todayIllnessViewModel.selectedDayData.deaths.value = todayIllnessInitializer.data.newDeathsList[selectedDay]
            todayIllnessViewModel.selectedDayData.recovered.value = todayIllnessInitializer.data.newRecoveredList[selectedDay]

            todayIllnessViewModel.calcIncrease(todayIllnessInitializer.data.newCasesList[selectedDay],  todayIllnessInitializer.data.newCasesList[selectedDay-1])
        }
    }
}