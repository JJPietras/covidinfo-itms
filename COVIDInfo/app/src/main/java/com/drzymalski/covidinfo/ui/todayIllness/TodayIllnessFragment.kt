package com.drzymalski.covidinfo.ui.todayIllness

import android.annotation.SuppressLint
import android.app.ActionBar
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.Gravity.END
import android.view.Gravity.RIGHT
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.marginLeft
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.drzymalski.covidinfo.R
import com.drzymalski.covidinfo.lib.FragmentBinder
import com.drzymalski.covidinfo.plottingUtils.TodayIllnessInitializer
import com.drzymalski.covidinfo.ui.selector.SelectorFragment
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAOptions
import kotlinx.android.synthetic.main.fragment_today.*
import kotlinx.coroutines.*


class TodayIllnessFragment : Fragment() {

    private lateinit var viewModel: TodayIllnessViewModel

    private var initializer: TodayIllnessInitializer = TodayIllnessInitializer()
    private var selectedDay: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this).get(TodayIllnessViewModel::class.java)
        return inflater.inflate(R.layout.fragment_today, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GlobalScope.launch {
            try { // Prevents crashing when data was loaded after changing or refreshing the fragment
                loadData()
                selectedDay = initializer.data.datesFullList.lastIndex - 1
                setData()
                buttonVisibility()
                configurateCharts()
                generateCountryButtons()
            }catch (ex: Exception){ // No action will be taken
                println(ex)
            }
        }

        configureObserver(viewModel.confirmed, statisticsCount)
        configureObserver(viewModel.died, statisticsDied)
        configureObserver(viewModel.recovered, statisticsCured)
        configureObserver(viewModel.date, statisticsDate)

        viewModel.nIncreaseCount.observe(viewLifecycleOwner, Observer {
            statisticsIncreaseNum.text = "${if (it > 0) "+" else ""}${it}"
            statisticsIncreaseNum.setTextColor(
                Color.parseColor(if (it < 0) "#388E3C" else "#C2185B")
            )
        })
        viewModel.pIncreaseCount.observe(viewLifecycleOwner, Observer {
            statisticsIncreasePercent.text = "${if (it > 0f) "+" else ""}${"%.1f".format(it)}%"
            statisticsIncreasePercent.setTextColor(
                Color.parseColor(if (it < 0f) "#388E3C" else "#C2185B")
            )
        })

        configureButton(statisticsPrevDay, -1, true)
        configureButton(statisticsNextDay, 1, false)

        FragmentBinder.bindToButton(
            view.findViewById(R.id.statisticsMenuBtn),
            SelectorFragment(),
            requireActivity()
        )
    }

    private fun configureButton(button: ImageButton, dayValue: Int, greater: Boolean) =
        button.setOnClickListener {
            val lastIndex = initializer.data.datesFullList.lastIndex
            val calculatedCondition = if (greater) selectedDay > 1 else selectedDay < lastIndex

            if (calculatedCondition) {
                selectedDay += dayValue
                setData()
            }
            buttonVisibility()
        }

    private fun configureChart(chart: AAChartView, options: AAOptions) {
        chart.post(Runnable {
            chart.aa_drawChartWithChartOptions(options)
        })
    }


    private fun configureObserver(liveData: LiveData<*>, textView: TextView) =
        liveData.observe(viewLifecycleOwner, Observer { textView.text = it.toString() })

    private fun buttonVisibility() {
        val currentDay = selectedDay == initializer.data.datesFullList.lastIndex - 1
        statisticsNextDay.visibility = if (currentDay) View.INVISIBLE else View.VISIBLE
        statisticsPrevDay.visibility = if (selectedDay == 1) View.INVISIBLE else View.VISIBLE
    }

    private fun configurateCharts(){
        configureChart(aaChartViewNewCases, initializer.configureNewCasesBarChart())
        configureChart(aaChartViewTotalCases, initializer.configureTotalCasesBarChart())
        configureChart(aaChartViewActiveCases, initializer.configureActiveBarChart())
        configureChart(aaChartViewRecovered, initializer.configureRecoveredBarChart())
        configureChart(aaChartViewDied, initializer.configureDeathsBarChart())
    }

    private fun setData() {
        if (selectedDay > 0 && selectedDay < initializer.data.datesFullList.lastIndex) {

            val data = initializer.data
            val newCasesList = data.newCasesList

            viewModel.dateLive.postValue(data.datesFullList[selectedDay + 1])
            viewModel.confirmedLive.postValue(data.newCasesList[selectedDay])
            viewModel.deathsLive.postValue(data.newDeathsList[selectedDay])
            viewModel.recoveredLive.postValue(data.newRecoveredList[selectedDay])

            viewModel.calcIncrease(
                newCasesList[selectedDay],
                newCasesList[selectedDay - 1]
            )
        }
    }

    private fun refresh(chart: AAChartView, data: MutableList<Int>){ // way to asynchronously refresh chart data plz do not delet
        val aaSeriesElementsArr: Array<AASeriesElement> = arrayOf(AASeriesElement()
            .data(data.toTypedArray()))
        chart.post(Runnable {
            chart.aa_onlyRefreshTheChartDataWithChartOptionsSeriesArray( //aa_chart function does not work
                aaSeriesElementsArr
            )
        })
    }

    private fun refresh(chart: AAChartView, options: AAOptions){
        chart.post(Runnable {
            chart.aa_refreshChartWithChartOptions( //aa_chart function does not work
                initializer.configureNewCasesBarChart()
            )
        })
    }

    private fun generateCountryButtons(){

        val button = Button(this.context)
        button.setBackgroundResource(R.drawable.sphere)
        button.textSize = 22f
        button.text = "PL"
        button.maxWidth = 58
        button.width = 100
        button.height = 100
        button.setTextColor(Color.parseColor("#FFFFFF"))
        button.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#F44336"))

        val parent = LinearLayout(context)
        parent.layoutParams = LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.WRAP_CONTENT)
        parent.orientation = LinearLayout.HORIZONTAL

        val child = LinearLayout(context)

        child.layoutParams = LinearLayout.LayoutParams(300,
            ActionBar.LayoutParams.WRAP_CONTENT)

        child.orientation = LinearLayout.VERTICAL

        val tvCountry = TextView(this.context)

        tvCountry.textSize = 20f
        tvCountry.text = "Polska"


        val tvContinent = TextView(this.context)

        tvContinent.textSize = 16f
        tvContinent.text = "Europa"

        val tvCases = TextView(this.context)

        tvCases.textSize = 20f
        tvCases.text = "24132 zakażeń"
        tvCases.gravity = RIGHT


        child.addView(tvCountry)
        child.addView(tvContinent)

        parent.addView(button)
        parent.addView(child)
        parent.addView(tvCases)

        statisticsCountriesLayout.post(Runnable {
            statisticsCountriesLayout.addView(parent)
        })
    }

    private fun loadData() {
        initializer.data.loadMainScreenResouces("2020-10-01")
    }
}