package com.drzymalski.covidinfo.lib

import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.drzymalski.covidinfo.R

class FragmentBinder {
    companion object {
        fun bindToButton(button: ImageButton, fragment: Fragment, activity: FragmentActivity) =
            button.setOnClickListener {
                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment)
                    .addToBackStack(null).commit()
            }
    }

}