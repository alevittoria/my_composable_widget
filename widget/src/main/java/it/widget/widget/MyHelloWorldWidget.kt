package it.widget.widget

import android.content.Context
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import androidx.glance.text.Text

object HelloWorldWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        Log.d("HelloWorldWidget", "provideGlance: id -> $id") // this is our widget identifier
        provideContent {
            Box(
                contentAlignment = Alignment.Center,
                modifier = GlanceModifier.background(color = Color.White).fillMaxSize()
            ) {
                Text("Hello, World!")
            }
        }
    }

}

class HelloWorldWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = HelloWorldWidget
}