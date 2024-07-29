package it.widget.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.icu.text.DateFormat
import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import it.widget.widget.receiver.TimeWidgetBroadcastReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

object TimeButtonWidget : GlanceAppWidget() {

    const val KEY_TS = "timestamp"
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        Log.d("TimeButtonWidget", "provideGlance: id -> $id") // this is our widget identifier


        provideContent {
            val timestamp = currentState(key = longPreferencesKey(KEY_TS)) ?: 0L
            val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
            val timeCal = Calendar.getInstance()
            timeCal.timeInMillis = timestamp
            val dateString = formatter.format(timeCal.time)
            Column(
                modifier = GlanceModifier.background(color = Color.White).fillMaxSize().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = GlanceModifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    Button(
                        text = "Send timestamp", onClick = actionRunCallback<UpdateTimestampAction>(
                            actionParametersOf(UpdateTimestampAction.tsParam to System.currentTimeMillis())
                        )
                    )
                }
                Box(
                    modifier = GlanceModifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(if (timestamp != 0L) dateString else "", style = androidx.glance.text.TextStyle(textAlign = TextAlign.Center))
                }
            }

        }
    }
}

class UpdateTimestampAction : ActionCallback {


    companion object {
        val tsParam = ActionParameters.Key<Long>("tsParam")
    }

    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        val ts = parameters[tsParam] ?: 0L
        updateAppWidgetState(context, glanceId) { prefs ->
            prefs[longPreferencesKey((TimeButtonWidget.KEY_TS))] = ts
        }
        TimeButtonWidget.update(context, glanceId)
        TimeWidgetBroadcastReceiver.updateTimestamp(context, ts)
    }

}

class UpdateTimestampReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = TimeButtonWidget

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        Log.d("UpdateTimestampReceiver", "onUpdate: ids -> ${appWidgetIds.joinToString(", ")}")
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }
}