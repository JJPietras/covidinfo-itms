package com.drzymalski.covidinfo.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.Toast
import com.drzymalski.covidinfo.MainActivity
import com.drzymalski.covidinfo.R
import com.drzymalski.covidinfo.ui.todayIllness.TodayIllnessFragment


/**
 * Implementation of App Widget functionality.
 */
class CasesWidget : AppWidgetProvider() {


    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
//    override fun onUpdate(
//        context: Context,
//        appWidgetManager: AppWidgetManager,
//        appWidgetIds: IntArray
//    ) {
//        // There may be multiple widgets active, so update all of them
//        for (appWidgetId in appWidgetIds) {
//            updateAppWidget(context, appWidgetManager, appWidgetId)
//        }
//    }

//    internal fun updateAppWidget(
//        context: Context,
//        appWidgetManager: AppWidgetManager,
//        appWidgetId: Int
//    ) {
//        val intent = Intent(context, MainActivity::class.java)
//        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
//
//        // Get the layout for the App Widget and attach an on-click listener
//        // to the button
//        val views = RemoteViews(context.packageName, R.layout.fragment_today)
//        views.setOnClickPendingIntent(R.id.newCasesNum, pendingIntent)
//
//        // Tell the AppWidgetManager to perform an update on the current app
//        // widget
//        appWidgetManager.updateAppWidget(appWidgetId, views)
//    }
}

