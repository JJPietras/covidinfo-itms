package com.drzymalski.covidinfo.ui.settings

import android.content.Context
import android.graphics.Color
import android.transition.Slide
import android.transition.TransitionManager
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import com.drzymalski.covidinfo.R
import kotlinx.android.synthetic.main.settings_layout.*

class SettingsView(context: Context, rootLayout: LinearLayout) {

    private var popupWindow: PopupWindow
    private var rootLayout: LinearLayout

    init {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val settingsView = inflater.inflate(R.layout.settings_layout,null)
        this.rootLayout = rootLayout
        popupWindow = PopupWindow(
            settingsView,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )

        popupWindow.elevation = 10.0F
        popupWindow.enterTransition = Slide().apply {
            slideEdge = Gravity.TOP
        }
        popupWindow.exitTransition = Slide().apply {
            slideEdge = Gravity.RIGHT
        }

        val tv = settingsView.findViewById<TextView>(R.id.text_view)
        val buttonPopup = settingsView.findViewById<Button>(R.id.button_popup)

        tv.setOnClickListener{
            tv.setTextColor(Color.RED)
        }

        buttonPopup.setOnClickListener{
            popupWindow.dismiss()
        }

    }

    fun show(){
        TransitionManager.beginDelayedTransition(rootLayout)
        popupWindow.showAtLocation(
            rootLayout,
            Gravity.CENTER,
            0,
            0
        )
    }

}