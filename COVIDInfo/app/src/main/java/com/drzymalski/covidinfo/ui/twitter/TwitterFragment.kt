package com.drzymalski.covidinfo.ui.twitter

import android.graphics.Color
import android.os.Bundle
import android.os.StrictMode
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.drzymalski.covidinfo.R
import com.drzymalski.covidinfo.lib.FragmentBinder
import com.drzymalski.covidinfo.ui.selector.SelectorFragment
import io.github.nandandesai.twitterscraper4j.TwitterScraper
import kotlinx.android.synthetic.main.fragment_tweeter.*


class TwitterFragment : Fragment() {

    private lateinit var twitterViewModel: TwitterViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        twitterViewModel = ViewModelProviders.of(this).get(TwitterViewModel::class.java)
        return inflater.inflate(R.layout.fragment_tweeter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FragmentBinder.bindToButton(
            view.findViewById(R.id.twitterMenuBtn),
            SelectorFragment(),
            requireActivity()
        )
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        getPosts()
    }

    fun getPosts(): Unit {
        val twitterScraper = TwitterScraper.builder().build()
        val tweets = twitterScraper.getUserTimeline("MZ_GOV_PL")
        for ((index,tweet) in tweets.withIndex()) {
            println(tweet)
            val card_view = CardView(requireContext())

            // Initialize a new LayoutParams instance, CardView width and height
            val cv_layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT, // CardView width
                LayoutParams.WRAP_CONTENT // CardView height
            )

            cv_layoutParams.setMargins(0,40,0,0)
            card_view.id = index
            card_view.layoutParams = cv_layoutParams
            card_view.setContentPadding(25,25,25,25)

            card_view.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary));
            card_view.radius = 50F
            twitterLayout.addView(card_view)
            //tło
            val imageView = ImageView(requireContext())

            imageView.setImageResource(R.drawable.side_nav_bar)

            imageView.layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )

            card_view.addView(imageView)
            //pierwszy LL
            val linearLayout = LinearLayout(requireContext())

            linearLayout.layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )

            linearLayout.setOrientation(LinearLayout.VERTICAL)

            card_view.addView(linearLayout)


            //drugi LL
            val linearLayout2 = LinearLayout(requireContext())

            linearLayout2.layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
            linearLayout2.setOrientation(LinearLayout.HORIZONTAL)
            linearLayout.addView(linearLayout2)
            //1 textView w drugim LL
            val textView12 = TextView(requireContext())
            val tv12_layoutParams = LayoutParams(
                LayoutParams.WRAP_CONTENT, // CardView width
                LayoutParams.WRAP_CONTENT // CardView height
            )
//            textView12.setPadding(25,10,25,10)
            tv12_layoutParams.setMargins(25,20,0,0)
            textView12.layoutParams = tv12_layoutParams
            val typeface = ResourcesCompat.getFont(requireContext(), R.font.roboto_medium)
            textView12.setTypeface(typeface)
            textView12.setTextSize(TypedValue.COMPLEX_UNIT_SP,20F);
            textView12.setTextColor(Color.parseColor("#7EFFFFFF"))
            textView12.text = "Ministerstwo Zdrowia"
            linearLayout2.addView(textView12)

            //textview do pierwszego LL
            val textView = TextView(requireContext())
            val tv_layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT, // CardView width
                LayoutParams.WRAP_CONTENT // CardView height
            )
            textView.setPadding(25,10,25,10)
            textView.layoutParams = tv_layoutParams
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,18F);
            textView.setTextColor(Color.parseColor("#CBFFFFFF"))
            textView.text = tweet.tweetText
            linearLayout.addView(textView)

        }
    }





}