package com.xwilarg.dailylearning

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import androidx.preference.PreferenceManager

/**
 * Implementation of App Widget functionality.
 */
class DailyLearningWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val views = RemoteViews(context.packageName, R.layout.daily_learning_widget)
    val preferences = UpdateInfo.updateJapaneseInfo(context.resources, context)

    views.setTextViewText(R.id.widgetWord, preferences.getString("currentWord", ""))
    views.setTextViewText(R.id.widgetReading, preferences.getString("currentReading", ""))
    views.setTextViewText(R.id.widgetMeaning, preferences.getString("currentMeanings", ""))

    appWidgetManager.updateAppWidget(appWidgetId, views)
}