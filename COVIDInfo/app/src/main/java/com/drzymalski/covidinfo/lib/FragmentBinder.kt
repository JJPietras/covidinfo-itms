package com.drzymalski.covidinfo.lib

import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import com.drzymalski.covidinfo.R


class FragmentBinder {
    companion object {

        private fun getAnimType(animateFromSide: Boolean): List<Int>{
            if (!animateFromSide) return listOf(R.anim.animate_in_top, R.anim.animate_out_top)
            return listOf(R.anim.animate_in_side, R.anim.animate_out_side)
        }

        fun bindToButton(button: Button, fragment: Fragment, activity: FragmentActivity, animateFromSide: Boolean=false) =
            button.setOnClickListener {
                val (aIn, aOut) = getAnimType(animateFromSide)
                activity.supportFragmentManager.beginTransaction()
                       // .setCustomAnimations(aIn, aOut, aIn, aOut)
                    .replace(R.id.nav_host_fragment, fragment)
                    .addToBackStack(null).commit()
            }

        fun bindToButton(button: ImageButton, fragment: Fragment, activity: FragmentActivity, animateFromSide: Boolean=false) =
            button.setOnClickListener {
                val (aIn, aOut) = getAnimType(animateFromSide)
                activity.supportFragmentManager.beginTransaction()
                   //     .setCustomAnimations(aIn, aOut, aIn, aOut)
                    .replace(R.id.nav_host_fragment, fragment)
                    .addToBackStack(null).commit()

            }
    }

}