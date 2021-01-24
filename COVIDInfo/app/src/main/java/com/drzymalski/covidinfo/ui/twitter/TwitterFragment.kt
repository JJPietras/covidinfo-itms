package com.drzymalski.covidinfo.ui.twitter

import android.annotation.SuppressLint
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
import kotlinx.android.synthetic.main.fragment_tweeter.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.NameValuePair
import org.apache.http.client.HttpClient
import org.apache.http.client.config.CookieSpecs
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.utils.URIBuilder
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicNameValuePair
import org.apache.http.util.EntityUtils
import org.json.JSONArray
import org.json.JSONObject
import org.ocpsoft.prettytime.PrettyTime
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URISyntaxException
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList


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

        FragmentBinder.bindNavToButton(
                view.findViewById<ImageButton>(R.id.twitterMenuBtn),
                view,
                R.id.action_nav_tweeter_to_nav_selector
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
        getUsers()
    }


    private fun getPosts() {
        try {
            val httpClient: HttpClient = HttpClients.custom()
                .setDefaultRequestConfig(
                    RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build()
                )
                .build()
            val uriBuilder =
                URIBuilder("https://api.twitter.com/2/tweets/search/recent?query=from:$curTTacc&tweet.fields=created_at&user.fields=created_at")
            val httpGet = HttpGet(uriBuilder.build())
            httpGet.setHeader(
                "Authorization",
                String.format(
                    "Bearer %s",
                    "AAAAAAAAAAAAAAAAAAAAADd5KAEAAAAAQeoc0ThNUcEYfdvCcciuxZTho%2BE%3DNIHR9ZRTqpHWx37V9ukK6Vsua4NX4sS8Ari3sL4HUkNvRvaFrn"
                )
            )
            val response: HttpResponse = httpClient.execute(httpGet)
            val entity: HttpEntity = response.entity
            val reader = BufferedReader(InputStreamReader(entity.content))
            val line: String = reader.readLine()


            Handler(Looper.getMainLooper()).post(Runnable(@SuppressLint("SetTextI18n")
            fun() {
                try {
                    val jsonObject = JSONObject(line)
                    val jArray: JSONArray = jsonObject.getJSONArray("data")
                    for (i in 0 until jArray.length()) {
                        val jsonObject1: JSONObject = jArray.getJSONObject(i)
                        val cardView = CardView(requireContext())
                        // Initialize a new LayoutParams instance, CardView width and height
                        val cvLayoutParams = LayoutParams(
                            LayoutParams.MATCH_PARENT, // CardView width
                            LayoutParams.WRAP_CONTENT // CardView height
                        )

<<<<<<< Updated upstream
                        cvLayoutParams.setMargins(0, 50, 0, 0)
=======
                        cvLayoutParams.setMargins(40, 70, 40, 35)
                        cardView.cardElevation = 50F
>>>>>>> Stashed changes
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

                        imageView.setImageResource(R.color.plotBgColor)

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
                        val p = PrettyTime(Locale("pl"))
                        //val ISO8601DATEFORMAT =  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ENGLISH)
                        textView22.text =
                            p.format(Date.from(Instant.parse(jsonObject1.optString("created_at"))))
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
                                    Uri.parse(
                                        "https://twitter.com/$curTTacc/status/" + jsonObject1.optString(
                                            "id"
                                        )
                                    )
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
                        textView.text = jsonObject1.optString("text")
                        linearLayout.addView(textView)
                    }
                } catch (ex: Exception){
                    println(ex.message)
                }
            }))
        } catch (ex: Exception){
            println(ex.message)
        }
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

/*    @Throws(IOException::class, URISyntaxException::class)
    private fun connectStream() {
        val httpClient: HttpClient = HttpClients.custom()
            .setDefaultRequestConfig(
                RequestConfig.custom()
                    .setCookieSpec(CookieSpecs.STANDARD).build()
            )
            .build()
        val uriBuilder =
            URIBuilder("https://api.twitter.com/2/tweets/search/recent?query=from:MZ_GOV_PL&tweet.fields=created_at&user.fields=created_at")
        val httpGet = HttpGet(uriBuilder.build())
        httpGet.setHeader("Authorization", String.format("Bearer %s", "AAAAAAAAAAAAAAAAAAAAADd5KAEAAAAAQeoc0ThNUcEYfdvCcciuxZTho%2BE%3DNIHR9ZRTqpHWx37V9ukK6Vsua4NX4sS8Ari3sL4HUkNvRvaFrn"))
        val response: HttpResponse = httpClient.execute(httpGet)
        val entity: HttpEntity = response.getEntity()
        val reader = BufferedReader(InputStreamReader(entity.getContent()))
        val line: String = reader.readLine()
            println(line)

    }*/

    @Throws(IOException::class, URISyntaxException::class)
    private fun getUsers() {
        try{
            var userResponse: String? = null
            val httpClient: HttpClient = HttpClients.custom()
                .setDefaultRequestConfig(
                    RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build()
                )
                .build()
            val uriBuilder = URIBuilder("https://api.twitter.com/2/users/by")
            val queryParameters: ArrayList<NameValuePair> = ArrayList()
            queryParameters.add(BasicNameValuePair("usernames", curTTacc))
            queryParameters.add(BasicNameValuePair("user.fields", "description,public_metrics"))
            uriBuilder.addParameters(queryParameters)
            val httpGet = HttpGet(uriBuilder.build())
            httpGet.setHeader(
                "Authorization",
                String.format(
                    "Bearer %s",
                    "AAAAAAAAAAAAAAAAAAAAADd5KAEAAAAAQeoc0ThNUcEYfdvCcciuxZTho%2BE%3DNIHR9ZRTqpHWx37V9ukK6Vsua4NX4sS8Ari3sL4HUkNvRvaFrn"
                )
            )
            httpGet.setHeader("Content-Type", "application/json")
            val response = httpClient.execute(httpGet)
            val entity = response.entity
            if (null != entity) {
                userResponse = EntityUtils.toString(entity, "UTF-8")
            }

            Handler(Looper.getMainLooper()).post {
                try {
                    if (userResponse!=null) {
                        val jsonObject = JSONObject(userResponse)
                        val jArray: JSONArray = jsonObject.getJSONArray("data")
                        for (i in 0 until jArray.length()) {
                            val jsonObject1: JSONObject = jArray.getJSONObject(i)
                            twitterAccountTitle?.text = jsonObject1.optString("name")
                            twitterAccountDescription?.text = jsonObject1.optString("description")
                            twitterAccountUsername?.text = jsonObject1.optString("username")
                            val newjsonobj: JSONObject = jsonObject1.getJSONObject("public_metrics")
                            twitterObservedCount?.text = newjsonobj.getString("following_count")
                            twitterObservesCount?.text = newjsonobj.getString("followers_count")
                            println(newjsonobj)
                        }
                    }
                }catch (ex: NullPointerException){
                    println(ex.message)
                }
            }
        }catch (ex: Exception){
            println(ex.message)
        }
    }
}