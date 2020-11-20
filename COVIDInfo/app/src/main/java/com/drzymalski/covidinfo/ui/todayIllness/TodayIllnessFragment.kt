package com.drzymalski.covidinfo.ui.todayIllness

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.drzymalski.covidinfo.R
import com.drzymalski.covidinfo.lib.FragmentBinder
import com.drzymalski.covidinfo.plottingUtils.TodayIllnessInitializer
import com.drzymalski.covidinfo.ui.selector.SelectorFragment
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAOptions
import kotlinx.android.synthetic.main.fragment_today.aaChartViewNewCases
import kotlinx.android.synthetic.main.fragment_today.statisticsDate
import kotlinx.android.synthetic.main.fragment_today.statisticsDied
import kotlinx.android.synthetic.main.fragment_today.statisticsCount
import kotlinx.android.synthetic.main.fragment_today.statisticsCured
import kotlinx.android.synthetic.main.fragment_today.statisticsIncreaseNum
import kotlinx.android.synthetic.main.fragment_today.statisticsIncreasePercent
import kotlinx.android.synthetic.main.fragment_today.statisticsNextDay
import kotlinx.android.synthetic.main.fragment_today.statisticsPrevDay
import kotlinx.android.synthetic.main.fragment_today.aaChartViewActiveCases
import kotlinx.android.synthetic.main.fragment_today.aaChartViewTotalCases
import kotlinx.android.synthetic.main.fragment_today.aaChartViewRecovered
import kotlinx.android.synthetic.main.fragment_today.aaChartViewDied
import kotlin.properties.Delegates

class TodayIllnessFragment : Fragment() {

    private lateinit var viewModel: TodayIllnessViewModel

    private var initializer: TodayIllnessInitializer = TodayIllnessInitializer()
    private var selectedDay by Delegates.notNull<Int>()

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

        initializer.data.loadMainScreenResouces("2020-10-01", "Poland")

        configureButton(statisticsPrevDay, -1, true)
        configureButton(statisticsNextDay, 1, false)

        configureChart(aaChartViewNewCases, initializer.configureNewCasesBarChart())
        configureChart(aaChartViewTotalCases, initializer.configureTotalCasesBarChart())
        configureChart(aaChartViewActiveCases, initializer.configureActiveBarChart())
        configureChart(aaChartViewRecovered, initializer.configureRecoveredBarChart())
        configureChart(aaChartViewDied, initializer.configureDeathsBarChart())

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

        selectedDay = initializer.data.datesFullList.lastIndex - 1

        setData()
        buttonVisibility()


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

    private fun configureChart(chart: AAChartView, options: AAOptions) =
        chart.aa_drawChartWithChartOptions(options)

    private fun configureObserver(liveData: LiveData<*>, textView: TextView) =
        liveData.observe(viewLifecycleOwner, Observer { textView.text = it.toString() })

    private fun buttonVisibility() {
        val currentDay = selectedDay == initializer.data.datesFullList.lastIndex - 1
        statisticsNextDay.visibility = if (currentDay) View.INVISIBLE else View.VISIBLE
        statisticsPrevDay.visibility = if (selectedDay == 1) View.INVISIBLE else View.VISIBLE
    }

    private fun setData() {
        if (selectedDay > 0 && selectedDay < initializer.data.datesFullList.lastIndex) {

            val data = initializer.data
            val newCasesList = data.newCasesList

            viewModel.dateLive.value = data.datesFullList[selectedDay + 1]
            viewModel.confirmedLive.value = data.newCasesList[selectedDay]
            viewModel.deathsLive.value = data.newDeathsList[selectedDay]
            viewModel.recoveredLive.value = data.newRecoveredList[selectedDay]

            viewModel.calcIncrease(
                newCasesList[selectedDay],
                newCasesList[selectedDay - 1]
            )
        }
    }
}