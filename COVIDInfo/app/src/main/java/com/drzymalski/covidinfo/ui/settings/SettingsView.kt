package com.drzymalski.covidinfo.ui.settings

import android.app.ActionBar
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.transition.Slide
import android.transition.TransitionManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import com.drzymalski.covidinfo.R
import com.drzymalski.covidinfo.config.CountryConfig
import com.drzymalski.covidinfo.data.Countries
import com.drzymalski.covidinfo.interfaces.FragmentSettings
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import com.toptoche.searchablespinnerlibrary.SearchableSpinner


class SettingsView(
    context: Context,
    rootLayout: LinearLayout,
    countries: MutableList<CountryConfig>,
    daysBack: Long,
    fragmentSettings: FragmentSettings
) {
    private var popupWindow: PopupWindow
    private var pickerOpen: Boolean = false
    private var rootLayout: LinearLayout
    private var statisticsCountriesLayout: LinearLayout
    private lateinit var callback: OnBackPressedCallback
    private var isClosed: Boolean = false
    private var fragmentSettings: FragmentSettings

    private var spinner: CustomSearchableSpinner
    private var addCountryBtn: Button
    private var saveBtn: Button
    private var closeBtn: Button
    private var daysPicker: NumberPicker
    private var context: Context
    private var selectedColor: String = "#5C6BC0"
    private var colorBtn: Button
    private var statsButton: AppCompatImageButton

    private var countriesNew:  MutableList<CountryConfig>
    private var daysBackNew: Int
    private val nullParent: ViewGroup? = null

    init {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val settingsView = inflater.inflate(R.layout.settings_layout, nullParent)

        this.rootLayout = rootLayout
        this.context = context
        this.fragmentSettings = fragmentSettings

        countriesNew = countries.toMutableList()
        daysBackNew = daysBack.toInt()

        daysPicker = settingsView.findViewById(R.id.daysPicker)
        colorBtn = settingsView.findViewById(R.id.addColorBtn)
        spinner = settingsView.findViewById(R.id.spinner_view)
        statisticsCountriesLayout = settingsView.findViewById(R.id.statisticsCountriesLayout)
        addCountryBtn = settingsView.findViewById(R.id.addCountryBtn)
        statsButton = settingsView.findViewById(R.id.statisticsChangeCountryBtn)

        popupWindow = PopupWindow(
            settingsView,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        ).apply {
            elevation = 10.0F
            enterTransition = Slide().apply { slideEdge = Gravity.TOP }
            exitTransition = Slide().apply { slideEdge = Gravity.END }
        }

        spinner.setTitle("Wybierz kraj")

        saveBtn = settingsView.findViewById(R.id.saveBtn)
        closeBtn = settingsView.findViewById(R.id.closeBtn)

        closeBtn.setOnClickListener{
            close()
            isClosed = true
        }

        saveBtn.setOnClickListener{
            close()
            isClosed = true
            this.daysBackNew = daysPicker.value
            this.fragmentSettings.applySettings(this.countriesNew, this.daysBackNew.toLong())
        }

        statsButton.setOnClickListener{ showCountries() }
        colorBtn.setOnClickListener{ displayColorPicker() }
        addCountryBtn.setOnClickListener{ addCountry() }

        generateCountryButtons()
        configurePickers()
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

    private fun showCountries(){
        if (statsButton.scaleY == 1f) {
            statsButton.scaleY = -1f
            statisticsCountriesLayout.visibility = GONE
        }
        else {
            statsButton.scaleY = 1f
            statisticsCountriesLayout.visibility = VISIBLE
        }
    }

    private fun addCountry(){
        val config =
            Countries.clist.find { "${it.name} ( ${it.code} )" == spinner.selectedItem }
        config?.color = selectedColor
        when {
            config == null -> {
                Toast.makeText(context, "Nie znaleziono kraju", Toast.LENGTH_SHORT).show()
            }
            config.slug in countriesNew.map { it.slug }.toTypedArray() -> {
                Toast.makeText(context, "Kraj jest już wybrany", Toast.LENGTH_SHORT).show()
            }
            countriesNew.size>8 -> {
                Toast.makeText(context, "Maksymalna liczba państw (9)", Toast.LENGTH_SHORT).show()
            }
            else -> {
                countriesNew.add(config)
                generateCountryButtons()
                Toast.makeText(context, "Dodano", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun generateCountryButtons(){
        try{
            statisticsCountriesLayout.post(kotlinx.coroutines.Runnable {
                statisticsCountriesLayout.removeAllViews()
            })

            countriesNew.forEach{ country ->

                    val button = Button(this.context).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            ActionBar.LayoutParams.MATCH_PARENT,
                            ActionBar.LayoutParams.WRAP_CONTENT, 0.6f
                        )
                        val shape = GradientDrawable()
                        shape.cornerRadius = 100f
                        background = shape
                        textSize = 22f
                        text = country.code
                        setTextColor(Color.parseColor("#FFFFFF"))
                        backgroundTintList = ColorStateList.valueOf(Color.parseColor(country.color))
                        setOnClickListener{
                            if (!pickerOpen){
                                ColorPickerDialogBuilder
                                    .with(context)
                                    .setTitle("Zmień kolor")
                                    .initialColor(Color.parseColor(country.color))
                                    .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                                    .density(12)
                                    .lightnessSliderOnly()
                                    .setPositiveButton(
                                        "OK"
                                    ) { _, selectedColor, _ ->
                                        country.color = "#" + Integer.toHexString(selectedColor).substring(
                                            2
                                        )
                                        backgroundTintList = ColorStateList.valueOf(selectedColor)
                                    }
                                    .setNegativeButton("Anuluj") { _, _ -> }
                                    .build()
                                    .show()
                                pickerOpen = true
                                }
                        enableColorPicker()
                        }
                    }

                    val parent = LinearLayout(context).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            ActionBar.LayoutParams.MATCH_PARENT,
                            ActionBar.LayoutParams.WRAP_CONTENT, 0.2f
                        )
                        orientation = LinearLayout.HORIZONTAL
                        setPadding(20, 5, 5, 5)
                    }

                    val child = LinearLayout(context).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            ActionBar.LayoutParams.MATCH_PARENT,
                            ActionBar.LayoutParams.WRAP_CONTENT, 0.4f
                        )
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
                        layoutParams = LinearLayout.LayoutParams(
                            ActionBar.LayoutParams.MATCH_PARENT,
                            ActionBar.LayoutParams.MATCH_PARENT, 0.75f
                        )

                        setBackgroundResource(R.drawable.ic_delete)
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
        if (countriesNew.count()>1) {
            countriesNew.removeAll{it.slug == slug }
            generateCountryButtons()
        }
        else{
            Toast.makeText(context, "Nie można usunąć wszystkich państw", Toast.LENGTH_SHORT).show()
        }
    }

    private fun configurePickers()  {
        daysPicker.minValue = 7
        daysPicker.maxValue = 365
        daysPicker.value = daysBackNew

        val searchMethod = ArrayAdapter(context,
            android.R.layout.simple_spinner_item,
            Countries.clist.map { "${it.name} ( ${it.code} )" }.toTypedArray()
        )
        searchMethod.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = searchMethod
    }

    fun close(){
        isClosed = true
        popupWindow.dismiss()
        callback.remove()
    }

    private fun displayColorPicker(){
        if (!pickerOpen){
            ColorPickerDialogBuilder
                .with(context)
                .setTitle("Wybierz kolor")
                .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                .density(12)
                .lightnessSliderOnly()
                .setPositiveButton(
                    "OK"
                ) { _, selectedColor, _ ->
                    this.selectedColor = "#" + Integer.toHexString(selectedColor).substring(2)
                    colorBtn.backgroundTintList = ColorStateList.valueOf(selectedColor)
                }
                .setNegativeButton("Anuluj") { _, _ -> }
                .build()
                .show()
            pickerOpen = true
        }
        enableColorPicker()
    }

    private fun enableColorPicker()=
        Handler().postDelayed(Runnable {
            pickerOpen = false
        }, 1500)
}