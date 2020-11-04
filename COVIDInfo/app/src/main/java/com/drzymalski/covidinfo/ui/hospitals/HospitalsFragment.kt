package com.drzymalski.covidinfo.ui.hospitals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.drzymalski.covidinfo.R

class HospitalsFragment : Fragment() {

    private lateinit var hospitalsViewModel: HospitalsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        hospitalsViewModel =
                ViewModelProviders.of(this).get(HospitalsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_hospitals, container, false)
        val textView: TextView = root.findViewById(R.id.text_hospitals)
        hospitalsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}