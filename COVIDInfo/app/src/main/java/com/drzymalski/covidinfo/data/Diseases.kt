package com.drzymalski.covidinfo.data

import com.drzymalski.covidinfo.R

object Diseases {
    val names = arrayOf(
        R.array.age,
        R.array.sex,
        R.array.ethnics,
        R.array.diabetes,
        R.array.cardio,
        R.array.lungs,
        R.array.cancer,
        R.array.rheumatologic,
        R.array.immuno
    )

    val severities = arrayOf(
        arrayOf(0, 1, 2, 4, 6),
        arrayOf(0, 1),
        arrayOf(0, 2, 1, 1, 1),
        arrayOf(1, 2, 1, 0),
        arrayOf(1, 2, 0),
        arrayOf(1, 2, 1, 0),
        arrayOf(3, 1, 0),
        arrayOf(2, 0),
        arrayOf(2, 0)
    )
}