package com.drzymalski.covidinfo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import com.application.isradeleon.notify.Notify;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        showNotification(context);

    }

    void showNotification (Context context) {
        Notify.build(context)

                /*
                 * Set notification title and content
                 * */
                .setTitle("COVID-19 w Polsce")
                .setContent("+6378 nowych zakażeń, +389 osób wyzdrowiało, +239 zmarło. Kliknij i zobacz najnowsze statystyki.")
                .setAction(new Intent(context, MainActivity.class))

                /*
                 * Set small icon from drawable resource
                 * */
                .setSmallIcon(R.drawable.icon)
                .setColor(R.color.colorPrimary)

                /*
                 * Set notification large icon from drawable resource or URL
                 * (make sure you added INTERNET permission to AndroidManifest.xml)
                 * */
                .setLargeIcon(R.drawable.icon)

                /*
                 * Circular large icon
                 * */
                .largeCircularIcon()

                /*
                 * Add a picture from drawable resource or URL
                 * (INTERNET permission needs to be added to AndroidManifest.xml)
                 * */

                .show(); // Show notification
    }
}