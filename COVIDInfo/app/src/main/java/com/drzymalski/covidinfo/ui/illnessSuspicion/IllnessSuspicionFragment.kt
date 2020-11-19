package com.drzymalski.covidinfo.ui.illnessSuspicion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.drzymalski.covidinfo.R
import com.drzymalski.covidinfo.ui.selector.SelectorFragment

class IllnessSuspicionFragment : Fragment() {

    private lateinit var illnessSuspicionViewModel: IllnessSuspicionViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        illnessSuspicionViewModel =
                ViewModelProviders.of(this).get(IllnessSuspicionViewModel::class.java)
        return inflater.inflate(R.layout.fragment_suspicion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val suspicionMenuBtn = view.findViewById<ImageButton>(R.id.suspicionMenuBtn)
        suspicionMenuBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, SelectorFragment())
                .addToBackStack(null).commit()
        }
    }
}