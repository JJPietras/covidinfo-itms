package com.drzymalski.covidinfo.ui.twitter

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.StrictMode
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.drzymalski.covidinfo.R
import com.drzymalski.covidinfo.lib.FragmentBinder
import com.drzymalski.covidinfo.ui.selector.SelectorFragment
import io.github.nandandesai.twitterscraper4j.TwitterScraper
import kotlinx.android.synthetic.main.fragment_tweeter.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception


class TwitterFragment : Fragment() {

    private lateinit var twitterViewModel: TwitterViewModel
    private var curTTacc = "MZ_GOV_PL"
    private var curTTcount = 0

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        twitterViewModel = ViewModelProvider(this).get(TwitterViewModel::class.java)
        return inflater.inflate(R.layout.fragment_tweeter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FragmentBinder.bindToButton(
            view.findViewById(R.id.twitterMenuBtn),
            SelectorFragment(),
            requireActivity()
        )
        twitterRefreshBtn.setOnClickListener {
            refresh()
        }
        twitterPreviousAccount.setOnClickListener {
            switch(2)
        }
        twitterNextAccount.setOnClickListener {
            switch(1)
        }

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        refresh()
    }

    private fun loadData() {
        Handler(Looper.getMainLooper()).post {
            try {
                val twitterScraper = TwitterScraper.builder().build()
                val profile = twitterScraper.getProfile(curTTacc)
                twitterAccountTitle.text = profile.name
                twitterAccountDescription.text = profile.description
                twitterAccountUsername.text = profile.username
                twitterObservedCount.text = profile.noOfFollowers.toString()
                twitterObservesCount.text = profile.noOfFriends.toString()
            }catch (ex: NullPointerException){
                println(ex.message)
            }
        }
    }

    private fun getPosts() {
        Handler(Looper.getMainLooper()).post(Runnable(fun() {
            val twitterScraper = TwitterScraper.builder().build()
            val tweets = twitterScraper.getUserTimeline(curTTacc)
            for (tweet in tweets) {
                val cardView = CardView(requireContext())
                // Initialize a new LayoutParams instance, CardView width and height
                val cvLayoutParams = LayoutParams(
                    LayoutParams.MATCH_PARENT, // CardView width
                    LayoutParams.WRAP_CONTENT // CardView height
                )

                cvLayoutParams.setMargins(0, 70, 0, 35)
                cardView.layoutParams = cvLayoutParams

                cardView.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorPrimary
                    )
                )
                cardView.radius = 100F
                twitterLayout.addView(cardView)
                //tło
                val imageView = ImageView(requireContext())

                imageView.setImageResource(R.drawable.side_nav_bar)

                imageView.layoutParams = LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT
                )

                cardView.addView(imageView)
                //pierwszy LL
                val linearLayout = LinearLayout(requireContext())

                linearLayout.layoutParams = LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT
                )

                linearLayout.orientation = LinearLayout.VERTICAL

                cardView.addView(linearLayout)


                //drugi LL
                val linearLayout2 = LinearLayout(requireContext())

                linearLayout2.layoutParams = LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT
                )
                linearLayout2.orientation = LinearLayout.HORIZONTAL
                linearLayout.addView(linearLayout2)

                //1 textView w drugim LL
                val textView12 = TextView(requireContext())
                val tv12LayoutParams = LayoutParams(
                    LayoutParams.WRAP_CONTENT, // CardView width
                    LayoutParams.WRAP_CONTENT // CardView height
                )
                tv12LayoutParams.setMargins(80, 60, 0, 0)

                textView12.layoutParams = tv12LayoutParams
                val typeface = ResourcesCompat.getFont(requireContext(), R.font.roboto_medium)
                textView12.typeface = typeface
                textView12.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
                textView12.setTextColor(Color.parseColor("#7EFFFFFF"))
                textView12.text = "@$curTTacc"
                linearLayout2.addView(textView12)

                //2 textView w drugim LL
                val textView22 = TextView(requireContext())
                val tv22LayoutParams = LayoutParams(
                    0, // CardView width
                    LayoutParams.WRAP_CONTENT,
                    0.1F // CardView height
                )
                tv22LayoutParams.setMargins(40, 60, 0, 0)
                tv22LayoutParams.weight = 0.1F
                textView22.layoutParams = tv22LayoutParams

                val typeface2 = ResourcesCompat.getFont(requireContext(), R.font.roboto)
                textView22.typeface = typeface2
                textView22.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)
                textView22.setTextColor(Color.parseColor("#C4FFFFFF"))
                textView22.text = tweet.timestamp
                linearLayout2.addView(textView22)

                //ikonka Twittera
                val imageButton = ImageButton(requireContext())
                val imgBtnParams = LayoutParams(
                    LayoutParams.WRAP_CONTENT, // CardView width
                    LayoutParams.WRAP_CONTENT
                )
                imgBtnParams.setMargins(0, 30, 100, 0)
                imgBtnParams.gravity = Gravity.END or Gravity.BOTTOM  // bottom-right
                imageButton.layoutParams = imgBtnParams
                imageButton.setImageResource(R.drawable.ic_twitter_small)
                imageButton.setBackgroundColor(Color.parseColor("#00FFFFFF"))

                imageButton.setOnClickListener {
                    val browserIntent =
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://twitter.com/" + curTTacc + "/status/" + tweet.tweetID)
                        )
                    startActivity(browserIntent)
                }
                linearLayout2.addView(imageButton)

                //textview do pierwszego LL
                val textView = TextView(requireContext())
                val tvLayoutParams = LayoutParams(
                    LayoutParams.MATCH_PARENT, // CardView width
                    LayoutParams.WRAP_CONTENT // CardView height
                )
                tvLayoutParams.setMargins(80, 0, 80, 0)
                textView.setPadding(0, 0, 0, 50)
                textView.layoutParams = tvLayoutParams
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18F)
                textView.setTextColor(Color.parseColor("#CBFFFFFF"))
                textView.text = tweet.tweetText
                linearLayout.addView(textView)
            }
        }))
    }

    private fun refresh(){
        try {
            twitterLayout.removeViews(1,twitterLayout.childCount -1)
            GlobalScope.launch {
                loadData()
                getPosts()
            }
        }catch (ex: Exception){
            println(ex.message)
        }
    }

    private fun switch(value: Int) {
        curTTcount += value
        when {
            curTTcount % 3 == 0 -> {
                curTTacc = "MZ_GOV_PL"
            }
            curTTcount % 3 == 1 -> {
                curTTacc = "GIS_gov"
            }
            curTTcount % 3 == 2 -> {
                curTTacc = "WHO"
            }
        }
        refresh()
    }




}