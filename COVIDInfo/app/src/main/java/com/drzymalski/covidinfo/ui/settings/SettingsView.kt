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
import com.drzymalski.covidinfo.data.Countries
import com.drzymalski.covidinfo.interfaces.FragmentSettings
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder


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

    private var spinner: Spinner
    private var addCountryBtn: Button
    private var saveBtn: Button
    private var closeBtn: Button
    private var daysPicker: NumberPicker
    private var context: Context
    private var selectedColor: String = "#5C6BC0"
    private var colorBtn: Button

    init {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val settingsView = inflater.inflate(R.layout.settings_layout, null)

        this.rootLayout = rootLayout
        this.context = context
        this.daysBack = daysBack
        this.fragmentSettings = fragmentSettings

        daysPicker = settingsView.findViewById(R.id.daysPicker)
        colorBtn = settingsView.findViewById(R.id.addColorBtn)
        spinner = settingsView.findViewById(R.id.spinner_view)
        statisticsCountriesLayout = settingsView.findViewById(R.id.statisticsCountriesLayout)
        addCountryBtn = settingsView.findViewById(R.id.addCountryBtn)

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

        colorBtn.setOnClickListener{
            displayColorPicker()
        }

        addCountryBtn.setOnClickListener{
           val config = Countries.clist.find { "${it.name} ( ${it.code} )" == spinner.selectedItem }
            config?.color = selectedColor
            when {
                config == null -> {
                    Toast.makeText(context, "Nie znaleziono kraju", Toast.LENGTH_SHORT).show()
                }
                config.slug in countries.map { it.slug }.toTypedArray() -> {
                    Toast.makeText(context, "Kraj jest już wybrany", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    countries.add(config)
                    generateCountryButtons()
                    Toast.makeText(context, "Dodano", Toast.LENGTH_SHORT).show()
                }
            }
        }



        daysPicker.minValue = 7
        daysPicker.maxValue = 365
        daysPicker.value = daysBack.toInt()
        generateCountryButtons()
        searchSpinner()
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
            statisticsCountriesLayout.post(kotlinx.coroutines.Runnable {
                statisticsCountriesLayout.removeAllViews()
            })

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
                        setOnClickListener{
                            ColorPickerDialogBuilder
                                .with(context)
                                .setTitle("Zmień kolor")
                                //.initialColor(Color.parseColor(selectedColor))
                                .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                                .density(12)
                                .lightnessSliderOnly()
                                .setOnColorSelectedListener { selectedColor ->
                                    //toast("onColorSelected: 0x" + Integer.toHexString(selectedColor))
                                }
                                .setPositiveButton("OK"
                                ) { dialog, selectedColor, allColors ->
                                    country.color = "#" + Integer.toHexString(selectedColor).substring(2)
                                    backgroundTintList = ColorStateList.valueOf(selectedColor)
                                }
                                .setNegativeButton("Anuluj"
                                ) { dialog, which -> }
                                .build()
                                .show()
                        }
                    }

                    val parent = LinearLayout(context).apply {
                        layoutParams = LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                            ActionBar.LayoutParams.WRAP_CONTENT, 0.2f)
                        orientation = LinearLayout.HORIZONTAL
                        setPadding(20, 5, 5, 5)
                    }

                    val child = LinearLayout(context).apply {
                        layoutParams = LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                            ActionBar.LayoutParams.WRAP_CONTENT, 0.4f)
                        orientation = LinearLayout.VERTICAL
                        setPadding(30, 0, 30, 0)
                    }

                    val tvCountry = TextView(this.context).apply {
                        textSize = 20f
                        text = country.name
                        setTextColor(Color.parseColor("#FFFFFF"))
                    }

                    val tvContinent = TextView(this.context).apply {
                        textSize = 14f
                        text = country.continent
                        setTextColor(Color.parseColor("#AAAAAA"))
                    }

                    val buttonDel = ImageButton(this.context).apply {
                        layoutParams = LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                            ActionBar.LayoutParams.MATCH_PARENT, 0.75f)

                        setBackgroundResource(R.drawable.ic_delete2)
                        backgroundTintList = ColorStateList.valueOf(Color.parseColor("#F44336"))
                        setOnClickListener{
                            deleteCountry(country.slug)
                        }
                    }

                    child.addView(tvCountry)
                    child.addView(tvContinent)

                    parent.addView(button)
                    parent.addView(child)
                    parent.addView(buttonDel)

                    statisticsCountriesLayout.post(kotlinx.coroutines.Runnable {
                        statisticsCountriesLayout.addView(parent)
                    })
                }

        }catch (ex: Exception){ // No action will be taken
            println(ex)
        }
    }

    private fun deleteCountry(slug: String){
        if (countries.count()>1) {
            countries.removeAll{it.slug == slug }
            generateCountryButtons()
        }
        else{
            Toast.makeText(context, "Nie można usunąć wszystkich państw", Toast.LENGTH_SHORT).show()
        }
    }

    private fun searchSpinner()  {
        val searchmethod = ArrayAdapter(context,
            android.R.layout.simple_spinner_item,
            Countries.clist.map { "${it.name} ( ${it.code} )" }.toTypedArray())
        searchmethod.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = searchmethod
    }

    fun close(){
        isClosed = true
        popupWindow.dismiss()
        callback.remove()
    }

    private fun displayColorPicker(){
        ColorPickerDialogBuilder
            .with(context)
            .setTitle("Wybierz kolor")
            //.initialColor(Color.parseColor(selectedColor))
            .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
            .density(12)
            .lightnessSliderOnly()
            .setOnColorSelectedListener { selectedColor ->
                //toast("onColorSelected: 0x" + Integer.toHexString(selectedColor))
            }
            .setPositiveButton("OK"
            ) { dialog, selectedColor, allColors ->
                this.selectedColor = "#" + Integer.toHexString(selectedColor).substring(2)
                colorBtn.backgroundTintList = ColorStateList.valueOf(selectedColor)
            }
            .setNegativeButton("Anuluj"
            ) { dialog, which -> }
            .build()
            .show()
    }

}