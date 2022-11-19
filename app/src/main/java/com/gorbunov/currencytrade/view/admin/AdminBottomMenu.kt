package com.gorbunov.currencytrade.view.admin

import android.graphics.drawable.VectorDrawable
import androidx.compose.material.*
import androidx.compose.material.icons.materialIcon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState

import com.gorbunov.currencytrade.R

sealed class BottomAdminNavItem(var title: String, var icon: Int, var screen_route: String) {

    object ApprovedList :
        BottomAdminNavItem("Подтвердить", R.drawable.ic_baseline_group_add_24, "approved_list")
    object BlockList:
            BottomAdminNavItem("Блокировка", R.drawable.ic_baseline_account_box_24, "block_list")

}

@Composable
fun AdminNavigationGraph(navController: NavHostController){
    NavHost(navController, startDestination = BottomAdminNavItem.ApprovedList.screen_route){
        composable(BottomAdminNavItem.ApprovedList.screen_route){
            val vm = hiltViewModel<AdminApprovedListViewModel>()
            AdminApprovedListView(vm = vm)
        }
        composable(BottomAdminNavItem.BlockList.screen_route){
            val vm = hiltViewModel<AdminBlockListViewModel>()
            AdminBlockListView(vm = vm)
        }
    }
}

@Composable
fun AdminBottomNavigation(navController: NavController){
    val items = listOf(
        BottomAdminNavItem.ApprovedList,
        BottomAdminNavItem.BlockList
    )

    BottomNavigation(
//        contentColor = Color.White, backgroundColor = MaterialTheme.colors.onBackground
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(ImageVector.vectorResource(id = item.icon), contentDescription = item.title)},
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