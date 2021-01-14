package com.drzymalski.covidinfo.ui.selector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.drzymalski.covidinfo.R
import com.drzymalski.covidinfo.lib.FragmentBinder
import com.drzymalski.covidinfo.ui.authors.AuthorsFragment
import com.drzymalski.covidinfo.ui.vaccine.VaccineFragment
import com.drzymalski.covidinfo.ui.hospitals.HospitalsFragment
import com.drzymalski.covidinfo.ui.suspicion.SuspicionFragment
import com.drzymalski.covidinfo.ui.compare.CompareFragment
import com.drzymalski.covidinfo.ui.todayIllness.TodayIllnessFragment
import com.drzymalski.covidinfo.ui.twitter.TwitterFragment
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
        val fragments = collectFragments()

        for (i in buttons.indices) {
            FragmentBinder.bindToButton(buttons[i], fragments[i], requireActivity())
        }
    }

    private fun collectButtons(): Array<Button> = arrayOf(
        selectorStatsFragBtn, selectorCompareBtn, selectorVaccineFragBtn, selectorTwitterFragBtn, selectorHospitalFragBtn,
        selectorSuspicionFragBtn, selectorAuthorsFragBtn
    )

    private fun collectFragments(): Array<Fragment> = arrayOf(
        TodayIllnessFragment(), CompareFragment(), VaccineFragment(), TwitterFragment(), HospitalsFragment(),
        SuspicionFragment(), AuthorsFragment()
    )
}