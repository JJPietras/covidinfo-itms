package com.drzymalski.covidinfo.ui.todayIllness

import android.annotation.SuppressLint
import android.app.ActionBar
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity.CENTER
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.drzymalski.covidinfo.R
import com.drzymalski.covidinfo.lib.FragmentBinder
import com.drzymalski.covidinfo.config.CountryConfig
import com.drzymalski.covidinfo.ui.selector.SelectorFragment
import com.drzymalski.covidinfo.ui.settings.SettingsView
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAOptions

import kotlinx.android.synthetic.main.fragment_today.*
import kotlinx.android.synthetic.main.fragment_today.aaChartViewActiveCases
import kotlinx.android.synthetic.main.fragment_today.aaChartViewDied
import kotlinx.android.synthetic.main.fragment_today.aaChartViewNewCases
import kotlinx.android.synthetic.main.fragment_today.aaChartViewRecovered
import kotlinx.android.synthetic.main.fragment_today.aaChartViewTotalCases
import kotlinx.android.synthetic.main.fragment_today.statisticsSettingsBtn
import kotlinx.android.synthetic.main.fragment_today.root_layout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch


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
            loadDataAndRefresh()
            generateCountryButtons()
        }

        configureObserver(viewModel.confirmed, statisticsCount)
        configureObserver(viewModel.died, statisticsDied)
        configureObserver(viewModel.recovered, statisticsCured)
        configureObserver(viewModel.date, statisticsDate)
        configureObserver(viewModel.countryCode, statisticsCountry)

        statisticsChangeCountryBtn.setOnClickListener{
            if (statisticsCountriesLayout.visibility == View.VISIBLE)
                statisticsCountriesLayout.visibility = View.GONE
            else
                statisticsCountriesLayout.visibility = View.VISIBLE
        }

        statisticsShowPoland.setOnClickListener {
            changeCountry(
                CountryConfig().apply {
                    slug = "poland"
                    name = "Polska"
                    continent = "Europa"
                    color = "#6f79fc"
                    code = "PL"
                }
            )
        }

        statisticsSettingsBtn.setOnClickListener{
            val settingsView = SettingsView(requireContext(), root_layout)
            settingsView.show()
            println("AA")
        }

        //statisticsCountriesLayout
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

            val lastIndex = initializer.data.stats.datesFullList.lastIndex
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
        val currentDay = selectedDay == initializer.data.stats.datesFullList.lastIndex - 1
        statisticsNextDay.visibility = if (currentDay) View.INVISIBLE else View.VISIBLE
        statisticsPrevDay.visibility = if (selectedDay == 1) View.INVISIBLE else View.VISIBLE
    }

    private fun configurateCharts(){
        try {
            configureChart(aaChartViewNewCases, initializer.configureNewCasesBarChart())
            configureChart(aaChartViewTotalCases, initializer.configureTotalCasesBarChart())
            configureChart(aaChartViewActiveCases, initializer.configureActiveBarChart())
            configureChart(aaChartViewRecovered, initializer.configureRecoveredBarChart())
            configureChart(aaChartViewDied, initializer.configureDeathsBarChart())
        }catch (ex: Exception){ // No action will be taken
            println(ex)
        }
    }

    private fun setData() {
        if (selectedDay > 0 && selectedDay < initializer.data.stats.datesFullList.lastIndex) {

            val data = initializer.data
            val newCasesList = data.stats.newCasesList

            viewModel.dateLive.postValue(data.stats.datesFullList[selectedDay + 1])
            viewModel.confirmedLive.postValue(data.stats.newCasesList[selectedDay])
            viewModel.deathsLive.postValue(data.stats.newDeathsList[selectedDay])
            viewModel.recoveredLive.postValue(data.stats.newRecoveredList[selectedDay])

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
        try{
            initializer.data.config.config.countries.forEach{ countryConfig ->
                val data = initializer.data.summaryData.Countries.find { countryConfig.slug == it.Slug }
                if (data != null){
                    val button = Button(this.context).apply {
                        layoutParams = LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                            ActionBar.LayoutParams.WRAP_CONTENT, 0.6f)
                        val shape = GradientDrawable()

                        shape.cornerRadius = 100f
                        background = shape
                        //setBackgroundResource(R.drawable.sphere) //Looks stretched when you rotate or have a weird aspect ratio
                        textSize = 22f
                        text = data.CountryCode
                        setTextColor(Color.parseColor("#FFFFFF"))
                        backgroundTintList = ColorStateList.valueOf(Color.parseColor(countryConfig.color))
                        setOnClickListener{
                            statisticsCountriesLayout.visibility = View.GONE
                            changeCountry(countryConfig)
                        }
                    }

                    val parent = LinearLayout(context).apply {
                        layoutParams = LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                            ActionBar.LayoutParams.WRAP_CONTENT, 0.2f)
                        orientation = LinearLayout.HORIZONTAL
                        setPadding(20,5,5,5)
                    }

                    val child = LinearLayout(context).apply {
                        layoutParams = LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                            ActionBar.LayoutParams.WRAP_CONTENT, 0.4f)
                        orientation = LinearLayout.VERTICAL
                        setPadding(30,0, 30,0)
                    }

                    val tvCountry = TextView(this.context).apply {
                        textSize = 20f
                        text = countryConfig.name
                        setTextColor(Color.parseColor("#373737"))
                    }

                    val tvContinent = TextView(this.context).apply {
                        textSize = 14f
                        text = countryConfig.continent
                    }

                    val tvCases = TextView(this.context).apply {
                        layoutParams = LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                            ActionBar.LayoutParams.WRAP_CONTENT, 0.4f)
                        gravity = CENTER
                        textSize = 20f
                        text = "${data.NewConfirmed} zakażeń"
                    }

                    child.addView(tvCountry)
                    child.addView(tvContinent)

                    parent.addView(button)
                    parent.addView(child)
                    parent.addView(tvCases)

                    statisticsCountriesLayout.post(Runnable {
                        statisticsCountriesLayout.addView(parent)
                    })
                }
            }
        }catch (ex: Exception){ // No action will be taken
            println(ex)
        }
    }

    private fun loadDataAndRefresh(){
        try { // Prevents crashing when data was loaded after changing or refreshing the fragment
            initializer.data.loadMainScreenResouces()
            selectedDay = initializer.data.stats.datesFullList.lastIndex - 1
            setData()
            buttonVisibility()
            configurateCharts()

        }catch (ex: Exception){ // No action will be taken
            println(ex)
        }
    }

    private fun changeCountry(countryConfig: CountryConfig){
        initializer.data.config.config.selectedCountry = countryConfig
        viewModel.codeLive.postValue(countryConfig.code)

        viewModel.dateLive.postValue("Wczytywanie")
        viewModel.confirmedLive.postValue(0)
        viewModel.deathsLive.postValue(0)
        viewModel.recoveredLive.postValue(0)
        viewModel.increaseCountLive.postValue(0)
        viewModel.increasePercentLive.postValue(0f)
        viewModel.calcIncrease(1, 1)

        GlobalScope.launch {
            loadDataAndRefresh()
        }
    }

}