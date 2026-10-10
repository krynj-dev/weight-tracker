package au.com.krnyj.weighttracker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import au.com.krnyj.weighttracker.ui.screen.HomeScreen
import au.com.krnyj.weighttracker.ui.screen.LogScreen

enum class Destination(
    val route: String,
    val label: String,
    val icon: Int,
    val contentDescription: String
) {
    HOME(
        "home",
        "Home",
        R.drawable.home_24dp_e3e3e3_fill0_wght500_grad0_opsz24,
        "Home"
    ),
    LOG(
        "log",
        "Log Weight",
        R.drawable.list_alt_24dp_e3e3e3_fill0_wght500_grad0_opsz24,
        "Log weight"
    ),
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: Destination,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController,
        startDestination = startDestination.route
    ) {
        Destination.entries.forEach { destination ->
            composable(destination.route) {
                when (destination) {
                    Destination.HOME -> HomeScreen()
                    Destination.LOG -> LogScreen(modifier = modifier)
                }
            }
        }
    }
}

@Composable
fun WTNavigationBar(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val startDestination = Destination.HOME
    var selectedLocation by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }

    Scaffold(modifier = modifier, bottomBar = {
        NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
            Destination.entries.forEachIndexed { index, location ->
                NavigationBarItem(
                    selected = selectedLocation == index,
                    onClick = {
                        navController.navigate(route = location.route)
                        selectedLocation = index
                    },
                    icon = {
                        Icon(
                            painter = painterResource(location.icon),
                            contentDescription = location.contentDescription
                        )
                    },
                    label = { Text(location.label) })
            }
        }
    }) { contentPadding ->
        AppNavHost(
            navController,
            startDestination,
            modifier = Modifier.padding(contentPadding)
        )
    }

}