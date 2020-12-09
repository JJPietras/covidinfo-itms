package com.drzymalski.covidinfo.ui.settings

import android.app.ActionBar
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.transition.Slide
import android.transition.TransitionManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatImageButton
import com.drzymalski.covidinfo.R
import com.drzymalski.covidinfo.config.CountryConfig
import com.drzymalski.covidinfo.interfaces.FragmentSettings

class SettingsView(
    context: Context,
    rootLayout: LinearLayout,
    countries: MutableList<CountryConfig>,
    daysBack: Long,
    fragmentSettings: FragmentSettings
) {

    private var popupWindow: PopupWindow

    private var rootLayout: LinearLayout
    private var statisticsCountriesLayout: LinearLayout
    private lateinit var callback: OnBackPressedCallback
    var isClosed: Boolean = false
    var fragmentSettings: FragmentSettings

    var countries:  MutableList<CountryConfig>
    var daysBack: Long

    private var saveBtn: Button
    private var closeBtn: Button
    private var daysPicker: NumberPicker
    private var context: Context

    init {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val settingsView = inflater.inflate(R.layout.settings_layout,null)
        this.rootLayout = rootLayout
        this.context = context
        this.daysBack = daysBack
        this.fragmentSettings = fragmentSettings
        daysPicker = settingsView.findViewById(R.id.daysPicker)
        statisticsCountriesLayout = settingsView.findViewById(R.id.statisticsCountriesLayout)

        popupWindow = PopupWindow(
            settingsView,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        this.countries = countries
        popupWindow.elevation = 10.0F
        popupWindow.enterTransition = Slide().apply {
            slideEdge = Gravity.TOP
        }
        popupWindow.exitTransition = Slide().apply {
            slideEdge = Gravity.RIGHT
        }

        saveBtn = settingsView.findViewById(R.id.saveBtn)
        closeBtn = settingsView.findViewById(R.id.closeBtn)

        closeBtn.setOnClickListener{
            close()
            isClosed = true
        }

        saveBtn.setOnClickListener{
            close()
            isClosed = true
            this.daysBack = daysPicker.value.toLong()
            this.fragmentSettings.applySettings(this.countries, this.daysBack)
        }

        val statsButton = settingsView.findViewById<AppCompatImageButton>(R.id.statisticsChangeCountryBtn)
        statsButton.setOnClickListener{
            if (statsButton.scaleY == 1f) {
                statsButton.scaleY = -1f
                statisticsCountriesLayout.visibility = GONE
            }
            else {
                statsButton.scaleY = 1f
                statisticsCountriesLayout.visibility = VISIBLE
            }
        }

        daysPicker.minValue = 7
        daysPicker.maxValue = 365
        daysPicker.value = daysBack.toInt()
        generateCountryButtons()
    }

    fun show(callback: OnBackPressedCallback){
        this.callback = callback
        isClosed = false
        TransitionManager.beginDelayedTransition(rootLayout)
        popupWindow.showAtLocation(
            rootLayout,
            Gravity.CENTER,
            0,
            0
        )
    }

    private fun generateCountryButtons(){
        try{
            countries.forEach{ country ->

                    val button = Button(this.context).apply {
                        layoutParams = LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                            ActionBar.LayoutParams.WRAP_CONTENT, 0.6f)
                        val shape = GradientDrawable()

                        shape.cornerRadius = 100f
                        background = shape
                        //setBackgroundResource(R.drawable.sphere) //Looks stretched when you rotate or have a weird aspect ratio
                        textSize = 22f
                        text = country.code
                        setTextColor(Color.parseColor("#FFFFFF"))
                        backgroundTintList = ColorStateList.valueOf(Color.parseColor(country.color))
                        /*setOnClickListener{
                            statisticsCountriesLayout.visibility = View.GONE
                            changeCountry(country)
                        }*/
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
                        text = country.name
                        setTextColor(Color.parseColor("#373737"))
                    }

                    val tvContinent = TextView(this.context).apply {
                        textSize = 14f
                        text = country.continent
                    }

                    /*val tvCases = TextView(this.context).apply {
                        layoutParams = LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                            ActionBar.LayoutParams.WRAP_CONTENT, 0.4f)
                        gravity = Gravity.CENTER
                        textSize = 20f
                        text = "${data.NewConfirmed} zakażeń"
                    }*/

                    child.addView(tvCountry)
                    child.addView(tvContinent)

                    parent.addView(button)
                    parent.addView(child)
                    //parent.addView(tvCases)

                    statisticsCountriesLayout.post(kotlinx.coroutines.Runnable {
                        statisticsCountriesLayout.addView(parent)
                    })
                }

        }catch (ex: Exception){ // No action will be taken
            println(ex)
        }
    }

    fun close(){
        isClosed = true
        popupWindow.dismiss()
        callback.remove()
    }
}