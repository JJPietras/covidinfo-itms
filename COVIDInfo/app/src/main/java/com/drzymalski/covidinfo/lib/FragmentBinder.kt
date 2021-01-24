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

        fun bindNavToButton(button: Button, view: View, resId: Int ) =
            button.setOnClickListener {
                Navigation.findNavController(view).navigate(resId)
            }

        fun bindNavToButton(button: ImageButton, view: View, resId: Int ) =
            button.setOnClickListener {
                Navigation.findNavController(view).navigate(resId)
            }

    }
}