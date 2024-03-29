package com.drzymalski.covidinfo.ui.todayIllness

import android.annotation.SuppressLint
import android.app.ActionBar
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Gravity.CENTER
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.drzymalski.covidinfo.R
import com.drzymalski.covidinfo.apiUtils.CSVManager
import com.drzymalski.covidinfo.config.CountryConfig
import com.drzymalski.covidinfo.dataUtils.PolandLoadedData
import com.drzymalski.covidinfo.interfaces.FragmentSettings
import com.drzymalski.covidinfo.lib.FragmentBinder
import com.drzymalski.covidinfo.ui.selector.SelectorFragment
import com.drzymalski.covidinfo.ui.settings.SettingsView
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.github.aachartmodel.aainfographics.aachartcreator.AAOptions
import kotlinx.android.synthetic.main.fragment_today.*
import kotlinx.android.synthetic.main.fragment_today.statisticsChangeCountryBtn
import kotlinx.android.synthetic.main.fragment_today.statisticsCountriesLayout
import kotlinx.android.synthetic.main.settings_layout.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch
import org.apache.http.client.methods.RequestBuilder.post
import java.time.LocalTime


@Suppress("DEPRECATION")
class TodayIllnessFragment : Fragment(), FragmentSettings {

    private lateinit var viewModel: TodayIllnessViewModel

    private var initializer: TodayIllnessInitializer = TodayIllnessInitializer()
    private var selectedDay: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this).get(TodayIllnessViewModel::class.java)
        return inflater.inflate(R.layout.fragment_today, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        generateCountryButtons()

        loadDataAndRefresh()
        refreshButtons()

        configureObserver(viewModel.confirmed, statisticsCount)
        configureObserver(viewModel.died, statisticsDied)
        configureObserver(viewModel.recovered, statisticsCured)
        configureObserver(viewModel.date, statisticsDate)
        configureObserver(viewModel.countryCode, statisticsChangeCountryBtn)

        viewModel.codeLive.postValue(initializer.config.config.selectedCountry.code)

        changeSelectCountryButtonColor()

        statisticsChangeCountryBtn.setOnClickListener{
            if (statisticsCountriesLayout.visibility == View.VISIBLE)
                statisticsCountriesLayout.visibility = View.GONE
            else
                statisticsCountriesLayout.visibility = View.VISIBLE
        }

        statisticsShowPoland.setOnClickListener { showPoland() }
        statisticsSettingsBtn.setOnClickListener{ showSettings() }

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

        FragmentBinder.bindNavToButton(
                view.findViewById<ImageButton>(R.id.statisticsMenuBtn),
                view,
                R.id.action_nav_today_to_nav_selector
        )

        activateLinks()

    }

    private fun configureButton(button: ImageButton, dayValue: Int, greater: Boolean) =
        button.setOnClickListener {
            val lastIndex = initializer.stats.datesFullList.lastIndex
            val calculatedCondition = if (greater) selectedDay > 1 else selectedDay < lastIndex
            if (calculatedCondition) {
                selectedDay += dayValue
                setData()
            }
            buttonVisibility()

        }

    private fun configureChart(chart: AAChartView, options: AAOptions) {
        chart.post(Runnable { chart.aa_drawChartWithChartOptions(options) })
    }

    private fun configureObserver(liveData: LiveData<*>, textView: TextView) =
        liveData.observe(viewLifecycleOwner, Observer { textView.text = it.toString() })

    private fun buttonVisibility() {
        val currentDay = selectedDay == initializer.stats.datesFullList.lastIndex - 1
        statisticsNextDay.visibility = if (currentDay) View.INVISIBLE else View.VISIBLE
        statisticsPrevDay.visibility = if (selectedDay == 1) View.INVISIBLE else View.VISIBLE
    }

    private fun configureCharts(){
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
        try {
            if (selectedDay > 0 && selectedDay < initializer.stats.datesFullList.lastIndex) {

                viewModel.calcIncrease(
                        initializer.stats.newCasesList[selectedDay],
                        initializer.stats.newCasesList[selectedDay - 1]
                )
                viewModel.dateLive.postValue(initializer.stats.datesFullList[selectedDay + 1])
                viewModel.confirmedLive.postValue(initializer.stats.newCasesList[selectedDay])
                viewModel.deathsLive.postValue(initializer.stats.newDeathsList[selectedDay])
                viewModel.recoveredLive.postValue(initializer.stats.newRecoveredList[selectedDay])
            }
        }catch (ex: Exception){ // No action will be taken
            println(ex)
        }
    }

    private fun generateCountryButtons(){
        try{
            statisticsCountriesLayout?.post(Runnable {
                statisticsCountriesLayout?.removeAllViews()
            })

            val tvBanner = TextView(this.context).apply {
                textSize = 30f
                text = context.getString(R.string.today_select_country)
                setTextColor(Color.parseColor("#91ABED"))
                gravity = CENTER
            }
            statisticsCountriesLayout?.post(Runnable {
                statisticsCountriesLayout?.addView(tvBanner)
            })

            initializer.config.config.countries.forEach{ countryConfig ->
                val data = initializer.newSummaryData?.dataProvider?.filter { countryConfig.code == it.iso3166_1 }

                var newCases:Int = 0

                if (data != null)
                    newCases = data[data.size.minus(1)].cnt_confirmed - data[data.size.minus(2)].cnt_confirmed

                val button = Button(this.context).apply {
                    layoutParams = LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                        ActionBar.LayoutParams.WRAP_CONTENT, 0.6f)

                    val shape = GradientDrawable()
                    shape.cornerRadius = 100f
                    background = shape
                    textSize = 22f
                    text = countryConfig.code
                    setTextColor(Color.parseColor("#FFFFFF"))
                    backgroundTintList = ColorStateList.valueOf(Color.parseColor(countryConfig.color))
                    setOnClickListener{
                        statisticsCountriesLayout?.visibility = View.GONE
                        changeCountry(countryConfig)
                    }
                }

                val parent = LinearLayout(context).apply {
                    layoutParams = LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                        ActionBar.LayoutParams.WRAP_CONTENT, 0.2f)
                    orientation = LinearLayout.HORIZONTAL
                    setPadding(20,5,5,5)
                    setOnClickListener{
                        statisticsCountriesLayout?.visibility = View.GONE
                        changeCountry(countryConfig)
                    }
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
                    setTextColor(Color.parseColor("#FFFFFF"))
                }

                val tvContinent = TextView(this.context).apply {
                    textSize = 14f
                    text = countryConfig.continent
                    setTextColor(Color.parseColor("#AAAAAA"))
                }

                val tvCases = TextView(this.context).apply {
                    layoutParams = LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                        ActionBar.LayoutParams.WRAP_CONTENT, 0.4f)
                    gravity = CENTER
                    textSize = 20f
                    setTextColor(Color.parseColor("#FFFFFF"))
                    text = if (countryConfig.code == "PL" && initializer.polandLoadedData.polandData.newCases != 0)
                        "${initializer.polandLoadedData.polandData.newCases} zakażeń"
                    else
                        if (newCases == 0) "" else "$newCases zakażeń"
                }

                child.addView(tvCountry)
                child.addView(tvContinent)

                parent.addView(button)
                parent.addView(child)
                parent.addView(tvCases)

                statisticsCountriesLayout?.post(Runnable {
                    statisticsCountriesLayout?.addView(parent)
                })
            }
        } catch (ex: Exception){ // No action will be taken
            println(ex)
        }
    }

    private fun loadDataAndRefresh(){
        GlobalScope.launch {
            try { // Prevents crashing when data was loaded after changing or refreshing the fragment
                initializer.loadMainScreenResources()

                selectedDay = initializer.stats.datesFullList.lastIndex - 1
                setData()
                buttonVisibility()
                configureCharts()
            }catch (ex: Exception){ // No action will be taken
                println(ex)
            }
        }
    }

    private fun showPoland(){
        var cnt = initializer.config.config.countries.find { it.code == "PL" }
        if (cnt==null) cnt = CountryConfig().apply {
            slug = "poland"
            name = "Polska"
            continent = "Europa"
            color = "#6f79fc"
            code = "PL"
        }
        changeCountry(cnt)
    }

    private fun showSettings(){
        val settingsView = SettingsView(
            requireContext(),
            root_layout,
            initializer.config.config.countries,
            initializer.config.config.daysBackToday,
            this
        )

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            settingsView.close()
        }
        settingsView.show(callback)
    }

    private fun changeCountry(countryConfig: CountryConfig){
        initializer.config.config.selectedCountry = countryConfig
        initializer.config.saveConfig()
        viewModel.codeLive.postValue(countryConfig.code)

        viewModel.dateLive.postValue("Wczytywanie")
        viewModel.confirmedLive.postValue(0)
        viewModel.deathsLive.postValue(0)
        viewModel.recoveredLive.postValue(0)
        viewModel.increaseCountLive.postValue(0)
        viewModel.increasePercentLive.postValue(0f)
        viewModel.calcIncrease(1, 1)

        changeSelectCountryButtonColor()

        loadDataAndRefresh()
    }

    override fun applySettings(countries: MutableList<CountryConfig>, daysBack: Long){
        this.initializer.config.config.countries = countries
        this.initializer.config.config.daysBackToday = daysBack
        this.initializer.config.config.selectedCountry.color =
                countries.find { it.code == this.initializer.config.config.selectedCountry.code }?.color
                ?: this.initializer.config.config.selectedCountry.color

        this.initializer.config.saveConfig( )

        loadDataAndRefresh()
        refreshButtons()
        changeSelectCountryButtonColor()
    }

    private fun refreshButtons(){
        GlobalScope.launch {
            initializer.loadSummaryData()
            generateCountryButtons()
        }
    }

    private fun activateLinks(){
        todaySource1.setOnClickListener {
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://documenter.getpostman.com/view/2220438/SzYevv9u?version=latest"))
            startActivity(browserIntent)
        }
        todaySource2.setOnClickListener {
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://www.gov.pl/web/koronawirus/wykaz-zarazen-koronawirusem-sars-cov-2"))
            startActivity(browserIntent)
        }
    }

    private fun changeSelectCountryButtonColor(){
        try{
            val color = ColorStateList.valueOf(Color.parseColor(initializer.config.config.selectedCountry.color))
            statisticsChangeCountryBtn.backgroundTintList = color
        }catch (ex: Exception){ // No action will be taken
            println(ex)
        }
    }




}