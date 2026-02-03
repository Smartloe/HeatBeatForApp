package com.tunehub.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.tunehub.app.ui.navigation.MainScaffold
import com.tunehub.app.ui.theme.TuneHubTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { TuneHubApp() }
    }
}

@Composable
private fun TuneHubApp() {
    TuneHubTheme {
        Surface {
            MainScaffold()
        }
    }
}
