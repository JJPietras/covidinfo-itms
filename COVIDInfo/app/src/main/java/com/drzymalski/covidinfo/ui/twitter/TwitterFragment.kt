package com.drzymalski.covidinfo.ui.twitter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        twitterViewModel.text.observe(viewLifecycleOwner, Observer { })
        return root
    }


}