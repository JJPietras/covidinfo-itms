package com.drzymalski.covidinfo.ui.twitter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.drzymalski.covidinfo.R

class TwitterFragment : Fragment() {

    private lateinit var twitterViewModel: TwitterViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        twitterViewModel =
                ViewModelProviders.of(this).get(TwitterViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_tweeter, container, false)
        val textView: TextView = root.findViewById(R.id.text_tweeter)
        twitterViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}