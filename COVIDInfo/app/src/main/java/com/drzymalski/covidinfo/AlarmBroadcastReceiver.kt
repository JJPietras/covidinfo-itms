package com.drzymalski.covidinfo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.application.isradeleon.notify.Notify
import com.drzymalski.covidinfo.config.ConfigurationManager
import com.drzymalski.covidinfo.dataUtils.DateConverter
import com.drzymalski.covidinfo.dataUtils.PolandLoadedData
import java.time.LocalTime

class AlarmBroadcastReceiver: BroadcastReceiver() {

    private val polandLoadedData = PolandLoadedData()
    private val config: ConfigurationManager = ConfigurationManager()

    override fun onReceive(context: Context?, intent: Intent?) {
        showNotification(context)
    }

    private fun showNotification(context: Context?) {
        polandLoadedData.loadPolandData()
        if (config.config.notifications && config.config.lastNotification != DateConverter.getTodayDate() && polandLoadedData.polandData.newCases != 0 && checkTime()){
            Notify.build(context!!) /*
                 * Set notification title and content
                 * */
                    .setTitle("COVID-19 w Polsce")
                    .setContent("+${polandLoadedData.polandData.newCases} nowych zakażeń, +${
                        polandLoadedData.polandData.recovered
                    } osób wyzdrowiało, +${
                        polandLoadedData.polandData.died
                    } zmarło. Kliknij i zobacz najnowsze statystyki.")
                    //.setContent("+666 nowych zakażeń, +666 osób wyzdrowiało, +666 zmarło. Kliknij i zobacz najnowsze statystyki.") // test czy sa wgl powiadomienia
                    .setAction(Intent(context, MainActivity::class.java)) /*
                 * Set small icon from drawable resource
                 * */
                    .setSmallIcon(R.drawable.icon)
                    .setColor(R.color.colorPrimary) /*
                 * Set notification large icon from drawable resource or URL
                 * (make sure you added INTERNET permission to AndroidManifest.xml)
                 * */
                    .setLargeIcon(R.drawable.icon) /*
                 * Circular large icon
                 * */
                    .largeCircularIcon() /*
                 * Add a picture from drawable resource or URL
                 * (INTERNET permission needs to be added to AndroidManifest.xml)
                 * */
                    .show() // Show notification
            config.config.lastNotification = DateConverter.getTodayDate()
            config.saveConfig()
        }
    }

    private fun checkTime(): Boolean{
        val target: LocalTime = LocalTime.parse("10:45:00")
        return LocalTime.now().isAfter(target)
    }
}