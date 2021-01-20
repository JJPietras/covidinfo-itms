package com.drzymalski.covidinfo.ui.settings

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.toptoche.searchablespinnerlibrary.SearchableSpinner

class CustomSearchableSpinner : SearchableSpinner {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var mLastClickTime: Long = 0

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            val lastClickTime = mLastClickTime
            val now = System.currentTimeMillis()
            mLastClickTime = now
            return if (now - lastClickTime < 500L) {
                // Too fast: ignore
                true
            } else {
                // Register the click
                super.onTouch(v, event)
            }
        }
        return true
    }
}