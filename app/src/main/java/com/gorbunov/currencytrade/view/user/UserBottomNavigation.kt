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

sealed class BottomUserNavItem(var title: String, var icon: Int, var screen_route: String) {

    object ApprovedList :
        BottomUserNavItem("Подтвердить", R.drawable.ic_baseline_group_add_24, "approved_list")
    object Profile:
        BottomUserNavItem("Профиль", R.drawable.ic_baseline_account_box_24, "profile")


}

@Composable
fun UserNavigationGraph(navController: NavHostController){
    NavHost(navController, startDestination = BottomUserNavItem.ApprovedList.screen_route){
        composable(BottomUserNavItem.ApprovedList.screen_route){
            val vm = hiltViewModel<AdminApprovedListViewModel>()
            AdminApprovedListView(vm = vm)
        }
        composable(BottomUserNavItem.Profile.screen_route){
            val vm = hiltViewModel<AdminBlockListViewModel>()
            AdminBlockListView(vm = vm)
        }
    }
}

@Composable
fun UserBottomNavigation(navController: NavController){
    val items = listOf(
        BottomUserNavItem.ApprovedList,
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