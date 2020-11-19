package com.drzymalski.covidinfo.ui.selector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.drzymalski.covidinfo.R


class SelectorFragment : Fragment() {

    private lateinit var selectorViewModel: SelectorViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        selectorViewModel =
            ViewModelProviders.of(this).get(SelectorViewModel::class.java)
        return inflater.inflate(R.layout.fragment_selector, container, false)
    }
}