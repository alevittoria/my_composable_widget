package it.widget.widget.util

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context

object WidgetUtils {
    fun getWidgetIds(context: Context, providerClass: Class<*>): IntArray {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val componentName = ComponentName(context, providerClass)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)
        return appWidgetIds
    }

}