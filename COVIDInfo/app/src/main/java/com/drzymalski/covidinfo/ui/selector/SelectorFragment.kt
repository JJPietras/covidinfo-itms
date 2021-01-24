package com.drzymalski.covidinfo.ui.selector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.drzymalski.covidinfo.R
import com.drzymalski.covidinfo.lib.FragmentBinder
import kotlinx.android.synthetic.main.fragment_selector.*


class SelectorFragment : Fragment() {

    private lateinit var selectorViewModel: SelectorViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        selectorViewModel =
            ViewModelProvider(this).get(SelectorViewModel::class.java)
        return inflater.inflate(R.layout.fragment_selector, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttons = collectButtons()
        val navigators = collectNavigators()

        for (i in buttons.indices) {
            FragmentBinder.bindNavToButton(buttons[i], view, navigators[i])
        }
    }

    private fun collectButtons(): Array<Button> = arrayOf(
        selectorStatsFragBtn, selectorCompareBtn, selectorVaccineFragBtn, selectorTwitterFragBtn, selectorHospitalFragBtn,
        selectorSuspicionFragBtn, selectorAuthorsFragBtn
    )

    private fun collectNavigators(): Array<Int> = arrayOf(
            R.id.action_nav_selector_to_nav_today, R.id.action_nav_selector_to_nav_compare,
            R.id.action_nav_selector_to_vaccineFragment, R.id.action_nav_selector_to_nav_tweeter,
            R.id.action_nav_selector_to_nav_hospitals, R.id.action_nav_selector_to_nav_suspicion,
            R.id.action_nav_selector_to_nav_authors
    )
}