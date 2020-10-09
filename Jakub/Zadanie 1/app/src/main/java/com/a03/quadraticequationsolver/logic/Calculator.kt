package com.a03.quadraticequationsolver.logic

import java.lang.NumberFormatException
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

class Calculator {

    fun calculateRoots(a: String, b: String, c: String): Array<Float>? {

        try {
            val numbers = arrayOf(a.toFloat(), b.toFloat(), c.toFloat())

            if (abs(numbers[0]) < 1) return null

            val discriminant = numbers[1].pow(2) - 4 * numbers[0] * numbers[2]
            val x1: Float
            val x2: Float

            if (discriminant > 0f) {
                val discriminantRoot = sqrt(discriminant)
                x1 = (-numbers[1] - discriminantRoot) / (2 * numbers[0])
                x2 = (-numbers[1] + discriminantRoot) / (2 * numbers[0])
            } else {
                x1 = -numbers[1] / (2 * numbers[0])
                x2 = x1
            }

            return arrayOf(x1, x2, discriminant)
        } catch (exception: NumberFormatException) {
            throw exception
        }
    }
}