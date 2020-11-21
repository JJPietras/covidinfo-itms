package com.drzymalski.covidinfo.ui.suspicion

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.provider.Settings
import android.view.View
import android.widget.ImageButton
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.drzymalski.covidinfo.lib.Hospital

class LocationManager(private val suspicionFragment: SuspicionFragment) {

    var requested: Boolean = false

    fun configureFindNearestHospitalButton(btn: ImageButton, view: View, act: FragmentActivity) {
        btn.setOnClickListener {
            val manager = act.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            checkIfProviderEnabled(manager, act)
            if (checkIfPermissionsNotGranted(view, act)) return@setOnClickListener
            else listen(manager)
        }
    }

    fun makeReturnAction(closestHospital: Hospital) =
        suspicionFragment.reactOnChange(closestHospital)

    @SuppressLint("MissingPermission")
    private fun listen(manager: LocationManager) {
        if (!requested) {
            requested = true
            val provider = LocationManager.NETWORK_PROVIDER
            val listener = SingularListener(this)
            manager.requestSingleUpdate(provider, listener, null)
        }
    }

    private fun checkIfProviderEnabled(manager: LocationManager, activity: FragmentActivity) {
        if (!manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            activity.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }

    private fun checkIfPermissionsNotGranted(view: View, activity: FragmentActivity): Boolean {
        val fineNotGranted = checkFinePermission(view) != PackageManager.PERMISSION_GRANTED
        val coarseNotGranted = checkCoarsePermission(view) != PackageManager.PERMISSION_GRANTED
        var notGranted = false

        if (fineNotGranted && coarseNotGranted) {
            val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
            activity.requestPermissions(permissions, 1)
            notGranted = true
        }
        return notGranted
    }

    private fun checkFinePermission(view: View) =
        ActivityCompat.checkSelfPermission(view.context, Manifest.permission.ACCESS_FINE_LOCATION)

    private fun checkCoarsePermission(view: View) =
        ActivityCompat.checkSelfPermission(view.context, Manifest.permission.ACCESS_COARSE_LOCATION)
}