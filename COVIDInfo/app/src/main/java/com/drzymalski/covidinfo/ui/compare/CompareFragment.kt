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
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.drzymalski.covidinfo.R
import com.drzymalski.covidinfo.lib.FragmentBinder
import com.drzymalski.covidinfo.config.CountryConfig
import com.drzymalski.covidinfo.ui.compare.CompareInitializer
import com.drzymalski.covidinfo.ui.selector.SelectorFragment
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAOptions
import kotlinx.android.synthetic.main.fragment_today.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class CompareFragment : Fragment() {

    private lateinit var viewModel: CompareViewModel
    private var initializer: CompareInitializer = CompareInitializer()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this).get(CompareViewModel::class.java)
        return inflater.inflate(R.layout.fragment_compare, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GlobalScope.launch {
            loadDataAndRefresh()
            generateCountryButtons()
        }

        statisticsChangeCountryBtn.setOnClickListener{
            if (statisticsCountriesLayout.visibility == View.VISIBLE)
                statisticsCountriesLayout.visibility = View.GONE
            else
                statisticsCountriesLayout.visibility = View.VISIBLE
        }

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formatted = current.format(formatter)

        statisticsDate.text = formatted
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

        FragmentBinder.bindToButton(
            view.findViewById(R.id.statisticsMenuBtn),
            SelectorFragment(),
            requireActivity()
        )
    }

    private fun configureChart(chart: AAChartView, options: AAOptions) {
        chart.post(Runnable {
            chart.aa_drawChartWithChartOptions(options)
        })
    }


    private fun configurateCharts(){
        configureChart(aaChartViewNewCases, initializer.configureNewCasesBarChart())
        configureChart(aaChartViewTotalCases, initializer.configureTotalCasesBarChart())
        configureChart(aaChartViewActiveCases, initializer.configureActiveBarChart())
        configureChart(aaChartViewRecovered, initializer.configureRecoveredBarChart())
        configureChart(aaChartViewDied, initializer.configureDeathsBarChart())
    }


    private fun generateCountryButtons(){
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
    }

    private fun loadDataAndRefresh(){
        try { // Prevents crashing when data was loaded after changing or refreshing the fragment
            initializer.data.loadScreenResouces()
            //selectedDay = initializer.data.stats.datesFullList.lastIndex - 1

            configurateCharts()

        }catch (ex: Exception){ // No action will be taken
            println(ex)
        }
    }

    private fun changeCountry(countryConfig: CountryConfig){
        initializer.data.config.config.selectedCountry = countryConfig

        GlobalScope.launch {
            loadDataAndRefresh()
        }
    }

}