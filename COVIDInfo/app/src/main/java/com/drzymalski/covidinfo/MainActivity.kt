package com.drzymalski.covidinfo

import android.app.AlarmManager
import android.app.AlarmManager.INTERVAL_DAY
import android.app.AlarmManager.INTERVAL_FIFTEEN_MINUTES
import android.app.PendingIntent

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import io.paperdb.Paper
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        Paper.init(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setRecurringAlarm()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun setRecurringAlarm() {

        // we know mobiletuts updates at right around 1130 GMT.
        // let's grab new stuff at around 11:45 GMT, inexactly
        val updateTime = Calendar.getInstance()
        updateTime[Calendar.HOUR_OF_DAY] = 11
        updateTime[Calendar.MINUTE] = 0
        updateTime[Calendar.SECOND] = 0
        updateTime[Calendar.MILLISECOND] = 0
        val intent = Intent(this, AlarmBroadcastReceiver::class.java)
        intent.putExtra("Powiadomienie Covid", "Covid Info")
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, updateTime.timeInMillis, INTERVAL_DAY, pendingIntent)

        /*val updateTimeTest = Calendar.getInstance()
        updateTime[Calendar.SECOND] = 5
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, updateTimeTest.timeInMillis, pendingIntent)*/
    }
}