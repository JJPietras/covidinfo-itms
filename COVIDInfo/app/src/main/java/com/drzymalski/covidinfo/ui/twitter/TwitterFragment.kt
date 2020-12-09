package com.drzymalski.covidinfo.ui.twitter

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageButton
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
        for (tweet in tweets) {
            println(tweet)
            val card_view = CardView(requireContext())

            // Initialize a new LayoutParams instance, CardView width and height
            val cv_layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT, // CardView width
                LayoutParams.WRAP_CONTENT // CardView height
            )

            cv_layoutParams.setMargins(0,40,0,0)
            card_view.layoutParams = cv_layoutParams
            card_view.setContentPadding(25,25,25,25)

            card_view.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
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

            linearLayout.orientation = LinearLayout.VERTICAL

            card_view.addView(linearLayout)


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
            val tv12_layoutParams = LayoutParams(
                LayoutParams.WRAP_CONTENT, // CardView width
                LayoutParams.WRAP_CONTENT // CardView height
            )
//            textView12.setPadding(25,10,25,10)
            tv12_layoutParams.setMargins(25,20,0,0)
            textView12.layoutParams = tv12_layoutParams
            val typeface = ResourcesCompat.getFont(requireContext(), R.font.roboto_medium)
            textView12.typeface = typeface
            textView12.setTextSize(TypedValue.COMPLEX_UNIT_SP,20F)
            textView12.setTextColor(Color.parseColor("#7EFFFFFF"))
            textView12.text = "Ministerstwo Zdrowia"
            linearLayout2.addView(textView12)

            //2 textView w drugim LL
            val textView22 = TextView(requireContext())
            val tv22_layoutParams = LayoutParams(
                0, // CardView width
                LayoutParams.WRAP_CONTENT,
                0.1F // CardView height
            )
//            textView12.setPadding(25,10,25,10)
            tv22_layoutParams.setMargins(50,20,0,0)
            textView22.layoutParams = tv22_layoutParams
            val typeface2 = ResourcesCompat.getFont(requireContext(), R.font.roboto)
            textView22.typeface = typeface2
            textView22.setTextSize(TypedValue.COMPLEX_UNIT_SP,16F)
            textView22.setTextColor(Color.parseColor("#C4FFFFFF"))
            textView22.text = tweet.timestamp
            linearLayout2.addView(textView22)

            //ikonka Twittera
            val imageButton = ImageButton(requireContext())
            val imgbtn_params = LayoutParams(
                LayoutParams.WRAP_CONTENT, // CardView width
                LayoutParams.WRAP_CONTENT
            )
//            textView12.setPadding(25,10,25,10)
            imgbtn_params.setMargins(0,0,20,-10)
            imgbtn_params.gravity = Gravity.END or Gravity.BOTTOM  // bottom-right
            imageButton.layoutParams = imgbtn_params
            imageButton.setImageResource(R.drawable.ic_twitter_small)
            imageButton.setBackgroundColor(Color.parseColor("#00FFFFFF"))

            imageButton.setOnClickListener(View.OnClickListener {
                val browserIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/MZ_GOV_PL/status/" + tweet.tweetID))
                startActivity(browserIntent) })
            linearLayout2.addView(imageButton)

            //textview do pierwszego LL
            val textView = TextView(requireContext())
            val tv_layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT, // CardView width
                LayoutParams.WRAP_CONTENT // CardView height
            )
            textView.setPadding(25,10,25,10)
            textView.layoutParams = tv_layoutParams
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,18F)
            textView.setTextColor(Color.parseColor("#CBFFFFFF"))
            textView.text = tweet.tweetText
            linearLayout.addView(textView)

        }
    }





}