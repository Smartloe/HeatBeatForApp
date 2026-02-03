package com.tunehub.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.tunehub.app.ui.navigation.AppNavGraph
import com.tunehub.app.ui.theme.TuneHubTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { TuneHubApp() }
    }
}

@Composable
private fun TuneHubApp() {
    val navController = rememberNavController()
    TuneHubTheme {
        Surface {
            AppNavGraph(navController = navController)
        }
    }
}
