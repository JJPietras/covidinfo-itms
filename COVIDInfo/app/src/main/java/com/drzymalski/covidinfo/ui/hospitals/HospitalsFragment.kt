package com.drzymalski.covidinfo.ui.hospitals

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.drzymalski.covidinfo.R
import com.drzymalski.covidinfo.data.Hospitals
import com.drzymalski.covidinfo.lib.FragmentBinder
import com.drzymalski.covidinfo.lib.Hospital
import com.drzymalski.covidinfo.ui.selector.SelectorFragment
import kotlinx.android.synthetic.main.fragment_hospitals.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.abs
import kotlin.random.Random


class HospitalsFragment : Fragment() {

    private lateinit var hospitalsViewModel: HospitalsViewModel

    private lateinit var images: Array<Drawable?>
    private var currentName: String = "NONE"
    private val regions = Hospitals.regionHospitals.keys
    private var currentList: List<Hospital>? = null
    private var cursor: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hospitalsViewModel =
            ViewModelProviders.of(this).get(HospitalsViewModel::class.java)
        return inflater.inflate(R.layout.fragment_hospitals, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FragmentBinder.bindToButton(
            view.findViewById(R.id.hospitalsMenuBtn),
            SelectorFragment(),
            requireActivity()
        )

        images = arrayOf(
            ResourcesCompat.getDrawable(resources, R.drawable.ic_small_hospital, null),
            ResourcesCompat.getDrawable(resources, R.drawable.ic_hospitals, null)
        )

        var count = 0
        for (region in Hospitals.regionHospitals)
            count += region.value.size

        hospitalsCount.text = count.toString()

        val url =
            URL("https://300gospodarka.pl/live/wolne-lozka-covid-19-wolne-respiratory-w-polsce")
        val urlConnection = url.openConnection() as HttpURLConnection

        GlobalScope.launch {
            val text: String
            try {
                text = urlConnection.inputStream.bufferedReader().readText()
            } finally {
                urlConnection.disconnect()
            }

            var htmlData = " 0  0 "
            try {
                val startIndex = text.indexOf("BlogPosting")
                val endIndex = text.indexOf("\"datePublished\": \"20")
                htmlData = text.substring(startIndex, endIndex)
            } catch (e: Exception) {
                Log.e("ERROR", "could not fetch hospital data")
            }

            val regex = " [0-9]+".toRegex()
            val match = regex.find(htmlData)
            val values = arrayOf(
                match?.value?.trim(),
                match?.next()?.value?.trim(),
                match?.next()?.next()?.value?.trim()
            )

            hospitalsUsedLSM.post {
                hospitalsUsedLSM.text = values[0]
            }

            hospitalsUsedBeds.post {
                hospitalsUsedBeds.text = values[1]
            }

            hospitalsUnUsedLSM.post {
                hospitalsUnUsedLSM.text = values[2]
            }
        }

        hospitalsRegionInput.addTextChangedListener {
            for (region in regions) {
                Log.wtf("HERE", "here " + hospitalsRegionInput.text.toString())
                val contains = region.contains(
                    hospitalsRegionInput.text.toString(),
                    ignoreCase = true
                )

                if (region != currentName && contains) {
                    currentName = region
                    currentList = Hospitals.regionHospitals[region]
                    switchHospital(0)
                    cursor = 0
                }
            }
        }

        hospitalsNextHospital.setOnClickListener { nextPrevHospital(true) }
        hospitalsPrevHospital.setOnClickListener { nextPrevHospital(false) }
    }

    private fun nextPrevHospital(next: Boolean) {
        if (currentList == null) return
        if (next) {
            if (cursor == currentList?.size!! - 1) {
                cursor = 0
            } else cursor++
        }
        else {
            if (cursor == 0) {
                cursor = currentList?.size!! - 1
            } else cursor--
        }
        switchHospital(cursor)
    }

    private fun switchHospital(index: Int) {
        val hospital = currentList?.get(index)
        hospitalsHospitalName.text = hospital?.name
        hospitalsHospitalTitle.text = hospital?.title
        hospitalsMainContact.text = hospital?.contact
        hospitalsMainCity.text = hospital?.city
        hospitalsMainIcon.setImageDrawable(images[abs(Random.nextInt() % 2)])
    }
}