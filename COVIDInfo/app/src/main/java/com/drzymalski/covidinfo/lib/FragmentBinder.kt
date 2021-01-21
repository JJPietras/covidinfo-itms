package com.drzymalski.covidinfo.lib

import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.drzymalski.covidinfo.R


class FragmentBinder {
    companion object {

        /*private fun getAnimType(animateFromSide: Boolean): List<Int>{
            if (!animateFromSide) return listOf(R.anim.animate_in_top, R.anim.animate_out_top)
            return listOf(R.anim.animate_in_side, R.anim.animate_out_side)
        }*/

        /*fun bindToButton(button: Button, fragment: Fragment, activity: FragmentActivity, animateFromSide: Boolean=false) =
            button.setOnClickListener {
                val (aIn, aOut) = getAnimType(true)

                activity.supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in, R.anim.fade_out,  R.anim.fade_in,  R.anim.slide_out)
                    .replace(R.id.nav_host_fragment, fragment)
                    .addToBackStack(null).commit()
            }*/

        fun bindNavToButton(button: Button, view: View, resId: Int ) =
            button.setOnClickListener {
                Navigation.findNavController(view).navigate(resId)
            }

        fun bindNavToButton(button: ImageButton, view: View, resId: Int ) =
                button.setOnClickListener {
                    Navigation.findNavController(view).navigate(resId)
                }

        /*fun bindToButton(button: ImageButton, fragment: Fragment, activity: FragmentActivity, animateFromSide: Boolean=false) =
            button.setOnClickListener {
                val (aIn, aOut) = getAnimType(true)
                activity.supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in, R.anim.fade_out,  R.anim.fade_in,  R.anim.slide_out)
                    .replace(R.id.nav_host_fragment, fragment)
                    .addToBackStack(null).commit()

            }*/
    }

}