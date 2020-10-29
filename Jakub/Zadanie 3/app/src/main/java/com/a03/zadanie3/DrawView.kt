package com.a03.zadanie3

import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import android.view.View

class DrawView(context: Context) : View(context), View.OnTouchListener {

    private val paint = Paint()
    private val points = mutableListOf<Point>()

    init {
        isFocusable = true
        isFocusableInTouchMode = true
        setOnTouchListener(this)
        paint.color = Color.RED
        val arr = floatArrayOf(0f, 1f, 0.5f)
        paint.maskFilter = EmbossMaskFilter(arr, 0.8f, 3f, 3f)
    }

    override fun onDraw(canvas: Canvas?) {
        for (point in points) {
            canvas?.drawCircle(point.x.toFloat(), point.y.toFloat(), 30F, paint)
        }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (event != null) {
            val point = Point()
            point.x = event.x.toInt()
            point.y = event.y.toInt()
            points.add(point)
            invalidate()
            Log.e("LOGGG", point.x.toString() + " " + point.y)
        }
        return true
    }

}