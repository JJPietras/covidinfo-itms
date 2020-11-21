package com.drzymalski.covidinfo.lib

import android.widget.Button

class SwitchableButton(button: Button, strings: Array<String>, private val values: Array<Int>) {
    private var currentIndex = -1

    init {
        button.setOnClickListener {
            currentIndex++
            if (currentIndex == strings.size) currentIndex = 0
            button.text = strings[currentIndex]
        }
    }

    fun getValue() = if (currentIndex != -1) values[currentIndex] else 0
}