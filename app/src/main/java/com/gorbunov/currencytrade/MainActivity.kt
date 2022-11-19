package com.gorbunov.currencytrade

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.gorbunov.currencytrade.ui.theme.CurrencyTradeTheme
import com.gorbunov.currencytrade.view.LoadingView
import com.gorbunov.currencytrade.view.LoginView
import com.gorbunov.currencytrade.view.LoginViewModel
import com.gorbunov.currencytrade.view.RegisterView
import com.gorbunov.currencytrade.view.admin.AdminBottomNavigation
import com.gorbunov.currencytrade.view.admin.AdminNavigationGraph
import com.gorbunov.currencytrade.view.admin.BottomAdminNavItem
import com.gorbunov.currencytrade.view.user.UserBottomNavigation
import com.gorbunov.currencytrade.view.user.UserNavigationGraph
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

enum class StartPosition{
    NotLogin, LoginAdmin, LoginUser, NeedRegister, Loading
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val currentStartState = rememberSaveable {
                mutableStateOf(StartPosition.NotLogin)
            }
            val navController = rememberNavController()

            val systemUiController = rememberSystemUiController()
            val useDarkIcons = MaterialTheme.colors.isLight
            val context = LocalContext.current

            CurrencyTradeTheme {
                BackPressSample()
                Crossfade(targetState = currentStartState) { state ->
                    when(state.value){
                        StartPosition.NotLogin -> {
                            val vm = hiltViewModel<LoginViewModel>()
                            LoginView(loginViewModel = vm, currentStartState)
                        }
                        StartPosition.LoginAdmin -> {
                            Scaffold(
                                modifier = Modifier.fillMaxSize(),
                                bottomBar = { AdminBottomNavigation(navController = navController)}
                            ) {
                                AdminNavigationGraph(navController = navController)
                            }
                        }
                        StartPosition.LoginUser -> {
                            Scaffold(
                                modifier = Modifier.fillMaxSize(),
                                bottomBar = { UserBottomNavigation(navController = navController) }
                            ) {
                                UserNavigationGraph(navController = navController)
                            }
                        }
                        StartPosition.NeedRegister -> {
                            val vm = hiltViewModel<LoginViewModel>()
                            RegisterView(loginViewModel = vm, startPosition = currentStartState)
                        }
                        StartPosition.Loading -> {
                            LoadingView()
                        }
                    }
                }


            }

            SideEffect {
                systemUiController.setSystemBarsColor(
                    color = Color.Transparent,
                    darkIcons = useDarkIcons
                )
            }
        }
    }
}


sealed class BackPress {
    object Idle : BackPress()
    object InitialTouch : BackPress()
}

@Composable
private fun BackPressSample() {
    var showToast by remember { mutableStateOf(false) }

    var backPressState by remember { mutableStateOf<BackPress>(BackPress.Idle) }
    val context = LocalContext.current

    if(showToast){
        Toast.makeText(context, "Нажмите еще раз чтобы выйти", Toast.LENGTH_SHORT).show()
        showToast= false
    }


    LaunchedEffect(key1 = backPressState) {
        if (backPressState == BackPress.InitialTouch) {
            delay(2000)
            backPressState = BackPress.Idle
        }
    }

    BackHandler(backPressState == BackPress.Idle) {
        backPressState = BackPress.InitialTouch
        showToast = true
    }
}
