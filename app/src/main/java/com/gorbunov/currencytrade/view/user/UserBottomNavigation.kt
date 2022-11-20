package com.gorbunov.currencytrade.view.user

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.gorbunov.currencytrade.R
import com.gorbunov.currencytrade.view.admin.AdminApprovedListView
import com.gorbunov.currencytrade.view.admin.AdminApprovedListViewModel
import com.gorbunov.currencytrade.view.admin.AdminBlockListView
import com.gorbunov.currencytrade.view.admin.AdminBlockListViewModel
import com.gorbunov.currencytrade.view.user.bag.BagView
import com.gorbunov.currencytrade.view.user.bag.BagViewModel
import com.gorbunov.currencytrade.view.user.profile.ProfileView
import com.gorbunov.currencytrade.view.user.profile.ProfileViewModel
import com.gorbunov.currencytrade.view.user.trade.TradeView
import com.gorbunov.currencytrade.view.user.trade.TradeViewModel
import com.gorbunov.currencytrade.view.user.wallet.CheckDetailView
import com.gorbunov.currencytrade.view.user.wallet.CheckDetailViewModel
import com.gorbunov.currencytrade.view.user.wallet.WalletView
import com.gorbunov.currencytrade.view.user.wallet.WalletViewModel

sealed class BottomUserNavItem(var title: String, var icon: Int, var screen_route: String) {

    object Wallet :
        BottomUserNavItem("Счета", R.drawable.ic_baseline_account_balance_wallet_24, "wallet")
    object Bag :
        BottomUserNavItem("Портфель", R.drawable.ic_round_cases_24, "bag")
    object Trade :
        BottomUserNavItem("Торговля", R.drawable.ic_baseline_add_chart_24, "trade")
    object CurrencyHistory:
        BottomUserNavItem("История", R.drawable.ic_baseline_show_chart_24, "CurrencyHistory")
    object Profile:
        BottomUserNavItem("Профиль", R.drawable.ic_baseline_account_box_24, "profile")


}

@Composable
fun UserNavigationGraph(navController: NavHostController){
    NavHost(navController, startDestination = BottomUserNavItem.Wallet.screen_route){
        composable(BottomUserNavItem.Wallet.screen_route){
            val vm = hiltViewModel<WalletViewModel>()
            WalletView(vm = vm, navController)
        }
        composable(BottomUserNavItem.Bag.screen_route){
            val vm = hiltViewModel<BagViewModel>()
            BagView(vm = vm)
        }
        composable(BottomUserNavItem.Trade.screen_route){
            val vm = hiltViewModel<TradeViewModel>()
            TradeView(vm = vm)
        }
        composable(BottomUserNavItem.CurrencyHistory.screen_route){
            val vm = hiltViewModel<AdminApprovedListViewModel>()
            AdminApprovedListView(vm = vm)
        }
        composable(BottomUserNavItem.Profile.screen_route){
            val vm = hiltViewModel<ProfileViewModel>()
            ProfileView(vm = vm)
        }
        composable("check_detail"){
            val vm = hiltViewModel<CheckDetailViewModel>()
            CheckDetailView(vm = vm)
        }
    }
}

@Composable
fun UserBottomNavigation(navController: NavController){
    val items = listOf(
        BottomUserNavItem.Wallet,
        BottomUserNavItem.Bag,
        BottomUserNavItem.Trade,
        BottomUserNavItem.CurrencyHistory,
        BottomUserNavItem.Profile
    )

    BottomNavigation(
        contentColor = Color.White, backgroundColor = MaterialTheme.colors.onBackground
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(ImageVector.vectorResource(id = item.icon), contentDescription = item.title) },
                selected = currentRoute == item.screen_route,
                label = {
                    Text(text = item.title, fontSize = 9.sp)
                },
                alwaysShowLabel = true,
                onClick = {
                    navController.navigate(item.screen_route){
                        navController.graph.startDestinationRoute?.let{screenRoute ->
                            popUpTo(screenRoute){
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}