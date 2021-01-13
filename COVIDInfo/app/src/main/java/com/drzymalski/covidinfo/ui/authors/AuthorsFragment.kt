package com.drzymalski.covidinfo.ui.authors

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.drzymalski.covidinfo.R
import com.drzymalski.covidinfo.lib.FragmentBinder
import com.drzymalski.covidinfo.ui.selector.SelectorFragment
import kotlinx.android.synthetic.main.fragment_authors.icon1
import kotlinx.android.synthetic.main.fragment_authors.icon2
import kotlinx.android.synthetic.main.fragment_authors.icon3
import kotlinx.android.synthetic.main.fragment_authors.icon4
import kotlinx.android.synthetic.main.fragment_authors.icon5
import kotlinx.android.synthetic.main.fragment_authors.icon6
import kotlinx.android.synthetic.main.fragment_authors.icon7
import kotlinx.android.synthetic.main.fragment_authors.icon8


class AuthorsFragment : Fragment() {

    private lateinit var authorsViewModel: AuthorsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        authorsViewModel =
                ViewModelProvider(this).get(AuthorsViewModel::class.java)
        return inflater.inflate(R.layout.fragment_authors, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val texts = arrayOf(icon1, icon2, icon3, icon4, icon5, icon6, icon7, icon8)
        for (text in texts) text.movementMethod = LinkMovementMethod.getInstance()

        FragmentBinder.bindToButton(
                view.findViewById<ImageButton>(R.id.authorsMenuBtn),
                SelectorFragment(),
                requireActivity()
        )
    }
}