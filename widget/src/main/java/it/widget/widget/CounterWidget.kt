package it.widget.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.intPreferencesKey
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
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.Text
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object CounterWidget : GlanceAppWidget() {

    const val KEY_COUNT = "count";
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        Log.d("CounterWidget", "provideGlance: id -> $id") // this is our widget identifier


        provideContent {
            val count = currentState(key = intPreferencesKey(KEY_COUNT)) ?: 0
            Row(
                modifier = GlanceModifier.background(color = Color.White).fillMaxSize().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    text = "-", onClick = actionRunCallback<ChangeCountAction>(
                        actionParametersOf(ChangeCountAction.countParam to count - 1)
                    )
                )
                Box(
                    modifier = GlanceModifier.defaultWeight(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(count.toString())
                }
                Button(
                    text = "+", onClick = actionRunCallback<ChangeCountAction>(
                        actionParametersOf(ChangeCountAction.countParam to count + 1)
                    )
                )
            }
        }
    }
}

class ChangeCountAction : ActionCallback {


    companion object {
        val countParam = ActionParameters.Key<Int>("count")

        fun invokeUpdateCounter(context: Context, glanceId: Int, count: Int) {
            val action = ChangeCountAction()
            CoroutineScope(Dispatchers.Main).launch {
                action.onAction(context, GlanceAppWidgetManager(context).getGlanceIdBy(glanceId), actionParametersOf(countParam to count))
            }
        }

    }

    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        updateAppWidgetState(context, glanceId) { prefs ->
            prefs[intPreferencesKey(CounterWidget.KEY_COUNT)] = parameters[countParam] ?: 0
        }
        CounterWidget.update(context, glanceId)
    }

}

class CounterWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = CounterWidget

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        Log.d("ChangeCountAction", "onUpdate: ids -> ${appWidgetIds.joinToString(", ")}")
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }
}