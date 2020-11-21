package com.drzymalski.covidinfo.ui.suspicion

import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import com.drzymalski.covidinfo.data.Hospitals
import com.drzymalski.covidinfo.lib.Hospital

class SingularListener(private val manager: LocationManager) : LocationListener {

    override fun onLocationChanged(p0: Location?) {
        showNearestHospital(p0!!)
        manager.requested = false
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {}
    override fun onProviderEnabled(p0: String?) {}
    override fun onProviderDisabled(p0: String?) {}

    private fun showNearestHospital(location: Location) {
        var closestHospital: Hospital? = null
        var shortestDistance = Float.MAX_VALUE

        for (region in Hospitals.regionHospitals) {
            for (hospital in region.value) {
                val hospitalLocation = createHospitalLocation(hospital)
                val currentDistance = hospitalLocation.distanceTo(location)
                if (currentDistance < shortestDistance) {
                    shortestDistance = currentDistance
                    closestHospital = hospital
                }
            }
        }
        if (closestHospital != null) manager.makeReturnAction(closestHospital)
    }

    private fun createHospitalLocation(hospital: Hospital): Location {
        val hospitalLocation = Location("")
        hospitalLocation.latitude = hospital.location[0]
        hospitalLocation.longitude = hospital.location[1]
        return hospitalLocation
    }
}