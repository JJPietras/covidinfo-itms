package com.drzymalski.covidinfo.plottingUtils

import java.util.*

class DateConverter {
    companion object{
        fun formatDateFull(date: Date): String{
            return (date.year + 1900).toString() + "-" + (date.month + 1).toString() + "-" + date.date.toString()
        }

        fun formatDateShort(date: Date): String{
            return (date.month + 1).toString() + "-" + date.date.toString()
        }

    }
}