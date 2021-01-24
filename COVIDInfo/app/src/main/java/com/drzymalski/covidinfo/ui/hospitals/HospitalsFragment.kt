package com.drzymalski.covidinfo.ui.hospitals

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.drzymalski.covidinfo.R
import com.drzymalski.covidinfo.data.Hospitals
import com.drzymalski.covidinfo.lib.FragmentBinder
import com.drzymalski.covidinfo.lib.Hospital
import kotlinx.android.synthetic.main.fragment_hospitals.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.abs
import kotlin.random.Random


class HospitalsFragment : Fragment() {

    private lateinit var hospitalsViewModel: HospitalsViewModel

    private lateinit var images: Array<Drawable?>
    private lateinit var imageIndexes: Array<Int>
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
            view.findViewById<ImageButton>(R.id.statisticsMenuBtn),
            SelectorFragment(),
            requireActivity()
        )

        images = arrayOf(
            ResourcesCompat.getDrawable(resources, R.drawable.ic_small_hospital, null),
            ResourcesCompat.getDrawable(resources, R.drawable.ic_hospitals, null)
        )

        imageIndexes = arrayOf(R.drawable.ic_small_hospital, R.drawable.ic_hospitals)


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

            hospitalsUsedLSM?.post {
                hospitalsUsedLSM?.text = values[0]
            }

            hospitalsUsedBeds?.post {
                hospitalsUsedBeds?.text = values[1]
            }

            hospitalsUnUsedLSM?.post {
                hospitalsUnUsedLSM?.text = (values[2]?.toInt()!! - values[0]?.toInt()!!).toString()
            }
        }

        hospitalsRegionInput?.addTextChangedListener {
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

        hospitalsNextHospital?.setOnClickListener { nextPrevHospital(true) }
        hospitalsPrevHospital?.setOnClickListener { nextPrevHospital(false) }

        hospitalsShowAll?.setOnClickListener {
            showAll()
        }
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
        hospitalsHospitalName?.text = hospital?.name
        hospitalsHospitalTitle?.text = hospital?.title
        hospitalsMainContact?.text = hospital?.contact
        hospitalsMainCity?.text = hospital?.city
        hospitalsMainIcon?.setImageDrawable(images[abs(Random.nextInt() % 2)])
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
        val wrapperLayout = ConstraintLayout(requireContext())
        wrapperLayout.id = View.generateViewId()
        val params = ConstraintLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        params.bottomMargin = 60
        wrapperLayout.layoutParams = params
        hospitalsLayout.addView(wrapperLayout)
        hospitalsLayout.visibility = View.VISIBLE

        //==============//
        //Hospital button
        //==============//

        val hospitalBtn = ImageButton(requireContext())
        wrapperLayout.addView(hospitalBtn)
        hospitalBtn.id = View.generateViewId()

        hospitalBtn.setOnClickListener { switchHospital(hospital) }
        hospitalBtn.setImageResource(imageIndexes[abs(Random.nextInt() % 2)])

        val hospitalBtnLayout = ConstraintLayout.LayoutParams(260, 260)
        hospitalBtn.layoutParams = hospitalBtnLayout //
        hospitalBtn.scaleType = ImageView.ScaleType.FIT_CENTER
        hospitalBtn.background = null


        //==============//
        //Hospital layout
        //==============//

        val typeface = ResourcesCompat.getFont(requireContext(), R.font.roboto_medium)
        val textLayout = LinearLayout(requireContext())

        wrapperLayout.addView(textLayout)
        textLayout.id = View.generateViewId()

        val textLayoutParams = ConstraintLayout.LayoutParams(
            0,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        textLayout.orientation = LinearLayout.VERTICAL
        textLayout.layoutParams = textLayoutParams//

        //First text

        val hospitalName = TextView(requireContext())
        textLayout.addView(hospitalName)
        val hospitalNameParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
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
        textLayout.addView(hospitalTitle)
        val hospitalTitleParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        hospitalTitle.layoutParams = hospitalTitleParams
        hospitalTitle.typeface = typeface
        hospitalTitle.setTextColor(Color.parseColor("#707070"))
        hospitalTitle.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 13F)
        hospitalTitle.text = hospital.title


        //Third text

        val hospitalRegionCity = TextView(requireContext())
        textLayout.addView(hospitalRegionCity)
        val hospitalRegionCityParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        hospitalRegionCity.layoutParams = hospitalRegionCityParams
        hospitalRegionCity.typeface = typeface
        hospitalRegionCity.setTextColor(Color.parseColor("#707070"))
        hospitalRegionCity.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 13F)
        hospitalRegionCity.text = hospital.city

        //Forth text

        val hospitalContact = TextView(requireContext())
        textLayout.addView(hospitalContact)
        val hospitalContactParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        hospitalContact.layoutParams = hospitalContactParams
        hospitalContact.typeface = typeface
        hospitalContact.setTextColor(Color.parseColor("#707070"))
        hospitalContact.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 13F)
        hospitalContact.text = hospital.contact

        //==============//
        //Google button
        //==============//

        val googleBtn = Button(requireContext())
        wrapperLayout.addView(googleBtn)
        googleBtn.id = View.generateViewId()

        googleBtn.setOnClickListener {
            val contact = hospital.contact
            val cords = hospital.location
            val route = contact.substring(0, contact.indexOf(','))
            val output = StringBuilder("geo:${cords[0]},${cords[1]}?q=")
            val s = ",+"

            output.append(hospital.city).append(s).append(hospital.name).append(s)
            output.append(hospital.title).append(s).append(route).append(s)
            val location = Uri.parse(output.toString())
            val mapIntent = Intent(Intent.ACTION_VIEW, location)
            val intentTitle = "Open with:"
            val chooser = Intent.createChooser(mapIntent, intentTitle)
            startActivity(chooser)
        }
        googleBtn.background = ResourcesCompat.getDrawable(resources, R.drawable.ic_map_point, null)

        val googleBtnLayout = ConstraintLayout.LayoutParams(168, 180)
        googleBtn.layoutParams = googleBtnLayout

        //Adding

        val layoutSet = ConstraintSet()
        layoutSet.clone(wrapperLayout)

        layoutSet.connect(
            hospitalBtn.id,
            ConstraintSet.BOTTOM,
            wrapperLayout.id,
            ConstraintSet.BOTTOM
        )
        layoutSet.connect(
            hospitalBtn.id,
            ConstraintSet.START,
            wrapperLayout.id,
            ConstraintSet.START
        )
        layoutSet.connect(hospitalBtn.id, ConstraintSet.TOP, wrapperLayout.id, ConstraintSet.TOP)
        layoutSet.connect(hospitalBtn.id, ConstraintSet.END, textLayout.id, ConstraintSet.START)

        layoutSet.connect(
            textLayout.id,
            ConstraintSet.BOTTOM,
            wrapperLayout.id,
            ConstraintSet.BOTTOM
        )
        layoutSet.connect(textLayout.id, ConstraintSet.END, googleBtn.id, ConstraintSet.START)
        layoutSet.connect(textLayout.id, ConstraintSet.START, hospitalBtn.id, ConstraintSet.END)
        layoutSet.connect(textLayout.id, ConstraintSet.TOP, wrapperLayout.id, ConstraintSet.TOP)

        layoutSet.connect(
            googleBtn.id,
            ConstraintSet.BOTTOM,
            wrapperLayout.id,
            ConstraintSet.BOTTOM
        )
        layoutSet.connect(googleBtn.id, ConstraintSet.END, wrapperLayout.id, ConstraintSet.END)
        layoutSet.connect(googleBtn.id, ConstraintSet.TOP, wrapperLayout.id, ConstraintSet.TOP)

   /*     layoutSet.setMargin(hospitalBtn.id, ConstraintSet.START, 40)
        layoutSet.setMargin(hospitalBtn.id, ConstraintSet.END, 40)*/

        layoutSet.setMargin(textLayout.id, ConstraintSet.START, -120)
        layoutSet.setMargin(textLayout.id, ConstraintSet.END, -120)

        layoutSet.setMargin(googleBtn.id, ConstraintSet.START, 40)
        layoutSet.setMargin(googleBtn.id, ConstraintSet.END, 40)

        layoutSet.applyTo(wrapperLayout)

        /*textLayout.requestLayout()
        wrapperLayout.requestLayout()
        hospitalsLayout.requestLayout()*/
    }
}