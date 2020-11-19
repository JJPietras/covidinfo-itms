package com.drzymalski.covidinfo.ui.hospitals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.drzymalski.covidinfo.R
import com.drzymalski.covidinfo.ui.selector.SelectorFragment


class HospitalsFragment : Fragment() {

    private lateinit var hospitalsViewModel: HospitalsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hospitalsViewModel =
            ViewModelProviders.of(this).get(HospitalsViewModel::class.java)
        return inflater.inflate(R.layout.fragment_hospitals, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val hospitalsMenuBtn = view.findViewById<ImageButton>(R.id.hospitalsMenuBtn)
        hospitalsMenuBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, SelectorFragment())
                .addToBackStack(null).commit()
        }
    }
}