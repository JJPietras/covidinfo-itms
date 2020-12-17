package com.drzymalski.covidinfo.ui.hospitals

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
    private var shown = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hospitalsViewModel =
            ViewModelProvider(this).get(HospitalsViewModel::class.java)
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

        initializeHospitals()

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
                hospitalsUnUsedLSM.text = (values[2]?.toInt()!! - values[0]?.toInt()!!).toString()
            }
        }

        hospitalsRegionInput.addTextChangedListener {
            for (region in regions) {
                val contains = region.contains(
                    hospitalsRegionInput.text.toString(),
                    ignoreCase = true
                )

                if (region != currentName && contains) {
                    currentName = region
                    currentList = Hospitals.regionHospitals[region]
                    switchHospitalByIndex(0)
                    cursor = 0
                    if (shown) showAll()
                }


            }
        }

        hospitalsNextHospital.setOnClickListener { nextPrevHospital(true) }
        hospitalsPrevHospital.setOnClickListener { nextPrevHospital(false) }

        hospitalsShowAll.setOnClickListener { showAll() }
    }

    private fun initializeHospitals() {
        currentList = Hospitals.regionHospitals["łódzkie"]
        switchHospitalByIndex(0)
    }

    private fun nextPrevHospital(next: Boolean) {
        if (currentList == null) return
        if (next) {
            if (cursor == currentList?.size!! - 1) {
                cursor = 0
            } else cursor++
        } else {
            if (cursor == 0) {
                cursor = currentList?.size!! - 1
            } else cursor--
        }
        switchHospitalByIndex(cursor)
    }

    private fun switchHospitalByIndex(index: Int) {
        val hospital = currentList?.get(index)
        switchHospital(hospital)
    }

    private fun switchHospital(hospital: Hospital?) {
        hospitalsHospitalName.text = hospital?.name
        hospitalsHospitalTitle.text = hospital?.title
        hospitalsMainContact.text = hospital?.contact
        hospitalsMainCity.text = hospital?.city
        hospitalsMainIcon.setImageDrawable(images[abs(Random.nextInt() % 2)])
    }

    private fun showAll() {
        if (currentList == null) return
        shown = true
        hospitalsLayout.removeAllViews()
        //hospitalsPlaceholderImg.visibility = View.GONE
        for (hospital in currentList!!) {
            createHospitalEntry(hospital)
        }
    }

    private fun createHospitalEntry(hospital: Hospital) {
        val wrapperLayout = LinearLayout(requireContext())
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        wrapperLayout.gravity = Gravity.CENTER
        wrapperLayout.layoutParams = params
        wrapperLayout.orientation = LinearLayout.HORIZONTAL
        wrapperLayout.weightSum = 1F

        //Hospital button

        val hospitalBtn = Button(requireContext())
        hospitalBtn.setOnClickListener { switchHospital(hospital) }
        hospitalBtn.background = images[abs(Random.nextInt() % 2)]
        hospitalBtn.scaleX = 0.5F
        hospitalBtn.scaleY = 0.5F

        val hospitalBtnLayout = LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            0.22F
        )
        hospitalBtn.layoutParams = hospitalBtnLayout
        /*hospitalBtn.width = 256*/
        hospitalBtn.minWidth = 58

        //Hospital layout

        val typeface = ResourcesCompat.getFont(requireContext(), R.font.roboto_medium)
        val textLayout = LinearLayout(requireContext())
        val textLayoutParams = LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            0.36F
        )

        textLayout.layoutParams = textLayoutParams
        textLayout.orientation = LinearLayout.VERTICAL

        //First text

        val hospitalName = TextView(requireContext())
        val hospitalNameParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            0.25F
        )
        hospitalNameParams.topMargin = 30
        hospitalName.layoutParams = hospitalNameParams
        hospitalName.typeface = typeface
        hospitalName.setTextColor(Color.parseColor("#373737"))
        hospitalName.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 13F)
        hospitalName.text = hospital.name
        //ew gravity

        //Second text

        val hospitalTitle = TextView(requireContext())
        val hospitalTitleParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            0.15F
        )
        hospitalTitle.layoutParams = hospitalTitleParams
        hospitalTitle.typeface = typeface
        hospitalTitle.setTextColor(Color.parseColor("#707070"))
        hospitalTitle.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 13F)
        hospitalTitle.text = hospital.title

        //Third text

        val hospitalContact = TextView(requireContext())
        val hospitalContactParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            0.25F
        )
        hospitalContact.layoutParams = hospitalContactParams
        hospitalContact.typeface = typeface
        hospitalContact.setTextColor(Color.parseColor("#707070"))
        hospitalContact.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 13F)
        hospitalContact.text = hospital.contact

        //TODO: add city and maybe region

        //Google button

        val googleBtn = Button(requireContext())
        googleBtn.setOnClickListener { switchHospital(hospital) }
        googleBtn.background = ResourcesCompat.getDrawable(resources, R.drawable.ic_map_point, null)
        googleBtn.scaleX = 0.7F
        googleBtn.scaleY = 0.7F

        val googleBtnLayout = LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            0.1F
        )
        hospitalBtn.layoutParams = googleBtnLayout

        //Adding

        textLayout.addView(hospitalName)
        textLayout.addView(hospitalTitle)
        textLayout.addView(hospitalContact)
        wrapperLayout.addView(hospitalBtn)
        wrapperLayout.addView(textLayout)
        wrapperLayout.addView(googleBtn)
        hospitalsLayout.addView(wrapperLayout)

        textLayout.requestLayout()
        wrapperLayout.requestLayout()
        hospitalsLayout.requestLayout()
    }
}