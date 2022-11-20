package com.gorbunov.currencytrade.view.user.bag

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gorbunov.currencytrade.view.LoadingView
import com.gorbunov.currencytrade.view.admin.AdminApprovedListViewModel
import com.himanshoe.charty.bar.BarChart
import com.himanshoe.charty.bar.model.BarData
import com.himanshoe.charty.common.axis.AxisConfig
import java.util.*

@Composable
fun BagView(vm: BagViewModel) {
    val rnd = Random()
    val curState = vm.currentState.collectAsState()

    Crossfade(targetState = curState) {
        when (val st = it.value) {
            is BagViewModel.State.Data ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
//                    item {
                        Text(
                            text = "Портфель",
                            modifier = Modifier.fillMaxWidth(),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
//                    }
                    val bars = mutableListOf<BarData>()
                    st.data.forEach { ch ->
                        bars.add(
                            BarData(
                                xValue = ch.currency_type,
                                yValue = ch.value.toFloat(),
                            )
                        )
                    }
//                    bars.add(BarData("lla", 24f))
//                    bars.add(BarData("llal", 27f))
                    val colors = mutableListOf<Color>()
                    bars.forEach{ _ ->
                        colors.add(Color.Red)
                    }
//                    item {
                        BarChart(
                            barData = bars,
                            onBarClick = {},
                            colors = colors,
                            modifier = Modifier
                                .fillMaxWidth()
                                .defaultMinSize(minHeight = 150.dp)
                                .padding(16.dp)
                        )
//                    }
                }
            is BagViewModel.State.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = st.e.message ?: "Неизвестная ошибка")
                }
            }
            BagViewModel.State.Loading -> {
                LoadingView()
            }
        }
    }


    BackHandler(curState.value is BagViewModel.State.Error) {
        vm.loadChecks()
    }
}