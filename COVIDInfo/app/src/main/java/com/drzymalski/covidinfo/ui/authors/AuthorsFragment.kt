package com.drzymalski.covidinfo.ui.authors

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.drzymalski.covidinfo.R
import kotlinx.android.synthetic.main.fragment_authors.icons2
import kotlinx.android.synthetic.main.fragment_authors.icons3
import kotlinx.android.synthetic.main.fragment_authors.icons4
import kotlinx.android.synthetic.main.fragment_authors.icons5
import kotlinx.android.synthetic.main.fragment_authors.icons6
import kotlinx.android.synthetic.main.fragment_authors.icons7
import kotlinx.android.synthetic.main.fragment_authors.icons8
import kotlinx.android.synthetic.main.fragment_authors.icons9


class AuthorsFragment : Fragment() {

    private lateinit var authorsViewModel: AuthorsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        authorsViewModel =
                ViewModelProviders.of(this).get(AuthorsViewModel::class.java)
        return inflater.inflate(R.layout.fragment_authors, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val texts = arrayOf(icons2, icons3, icons4, icons5, icons6, icons7, icons8, icons9)
        for (text in texts) text.movementMethod = LinkMovementMethod.getInstance()
    }
}