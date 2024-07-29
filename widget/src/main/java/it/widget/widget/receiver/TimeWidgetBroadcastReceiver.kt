package it.widget.widget.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
class TimeWidgetBroadcastReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {
        Log.d("TimeWidgetBroadcastReceiver", "onReceive: ${intent.action}")
        if (intent.action == "${context.packageName}.${UPDATE_TIMESTAMP_ACTION}") {
            val timestamp = intent.getLongExtra(TIMESTAMP_EXTRA, 0)
            Log.d("TimeWidgetBroadcastReceiver", "onReceive: timestamp -> $timestamp")
            GlobalScope.launch {
                context.dataStore.edit {
                    it[longPreferencesKey(TIMESTAMP_PREF_KEY_EXTRA)] = timestamp
                }
            }
        }
    }

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "time_widget_datastore")

        const val UPDATE_TIMESTAMP_ACTION = "UPDATE_TIMESTAMP"
        const val TIMESTAMP_EXTRA = "timestamp_extra"
        const val TIMESTAMP_PREF_KEY_EXTRA = "timestamp"


        fun updateTimestamp(context: Context, timestamp: Long) {
            context.sendBroadcast(Intent(context, TimeWidgetBroadcastReceiver::class.java).apply {
                action = "${context.packageName}.${UPDATE_TIMESTAMP_ACTION}"
                putExtra(TIMESTAMP_EXTRA, timestamp)
            })
        }

        fun getTimestamp(context: Context): Flow<Long> {
            return context.dataStore.data.map {
                it[longPreferencesKey(TIMESTAMP_PREF_KEY_EXTRA)] ?: 0
            }
        }
    }
}