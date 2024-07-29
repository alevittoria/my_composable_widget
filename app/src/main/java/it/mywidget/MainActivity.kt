package it.mywidget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import it.mywidget.ui.theme.MyWidgetTheme
import it.widget.widget.ChangeCountAction
import it.widget.widget.CounterWidget
import it.widget.widget.CounterWidgetReceiver
import it.widget.widget.receiver.TimeWidgetBroadcastReceiver
import it.widget.widget.util.WidgetUtils.getWidgetIds
import java.text.DateFormat
import java.util.Calendar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyWidgetTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Example(

                    )
                }
            }
        }
    }
}

@Composable
fun Example() {
    val context = LocalContext.current

    val ts = TimeWidgetBroadcastReceiver.getTimestamp(LocalContext.current).collectAsStateWithLifecycle(0)
    val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
    val timeCal = Calendar.getInstance()
    timeCal.timeInMillis = ts.value
    val dateString = formatter.format(timeCal.time)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            getWidgetIds(context = context, providerClass = CounterWidgetReceiver::class.java).forEach { appWidgetId ->
                ChangeCountAction.invokeUpdateCounter(context = context, glanceId = appWidgetId, count = 0)
            }
        }) {
            Text(text = "Reset to 0 all counter widgets")
        }
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = "Widget Timestamp: ${dateString}")
    }
}


