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
                .setTitle("Najnowsze statystyki dla Polski")
                .setContent("Umarło dużo zachorowało dużo wyzdrowiało mało")
                .setAction(new Intent(context, MainActivity.class))

                /*
                 * Set small icon from drawable resource
                 * */
                .setSmallIcon(R.drawable.ic_settings)
                .setColor(R.color.colorPrimary)

                /*
                 * Set notification large icon from drawable resource or URL
                 * (make sure you added INTERNET permission to AndroidManifest.xml)
                 * */
                .setLargeIcon("https://images.pexels.com/photos/139829/pexels-photo-139829.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=150&w=440")

                /*
                 * Circular large icon
                 * */
                .largeCircularIcon()

                /*
                 * Add a picture from drawable resource or URL
                 * (INTERNET permission needs to be added to AndroidManifest.xml)
                 * */
                .setPicture("https://images.pexels.com/photos/1058683/pexels-photo-1058683.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940")

                .show(); // Show notification
    }
}