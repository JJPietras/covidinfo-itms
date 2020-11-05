package com.drzymalski.covidinfo.ui.illnessSuspicion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.drzymalski.covidinfo.R

class IllnessSuspicionFragment : Fragment() {

    private lateinit var illnessSuspicionViewModel: IllnessSuspicionViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        illnessSuspicionViewModel =
                ViewModelProviders.of(this).get(IllnessSuspicionViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_suspicion, container, false)
        val textView: TextView = root.findViewById(R.id.text_suspicion)
        illnessSuspicionViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}