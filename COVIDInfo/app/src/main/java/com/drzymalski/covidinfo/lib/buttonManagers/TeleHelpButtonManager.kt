package com.drzymalski.covidinfo.lib.buttonManagers

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.Button
import androidx.fragment.app.FragmentActivity
import com.drzymalski.covidinfo.R

class TeleHelpButtonManager {
    fun configureTeleHelpButton(view: View, activity: FragmentActivity) {
        val teleHelpBtn = view.findViewById<Button>(R.id.suspicionTeleHelpBtn)
        teleHelpBtn.setOnClickListener {
            val webAddress = Uri.parse("https://pacjent.gov.pl/ewizyta")
            val gotoGoogle = Intent(Intent.ACTION_VIEW, webAddress)
            val chooser = Intent.createChooser(gotoGoogle, "Open with:")
            activity.startActivity(chooser)
        }
    }
}