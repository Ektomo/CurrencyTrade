package com.gorbunov.currencytrade.view.user.trade

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gorbunov.currencytrade.view.LoadingView

@Composable
fun TradeView(vm: TradeViewModel) {

    val curState = vm.currentState.collectAsState()

    Crossfade(targetState = curState) {
        when (val st = it.value) {
            is TradeViewModel.State.Data -> LazyColumn(
                modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(
                    bottom = 70.dp, top = 16.dp, start = 16.dp, end = 16.dp
                )
            ) {


                item {
//                    var isFirst = true
                    var expandedSelf by remember { mutableStateOf(false) }
                    var selectedIndexSelf by remember {
                        mutableStateOf(0)
                    }
                    var expanded by remember { mutableStateOf(false) }
                    var selectedIndex by rememberSaveable {
                        mutableStateOf(0)
                    }
                    var sum by remember {
                        mutableStateOf("0")
                    }

//                    LaunchedEffect(key1 = selectedIndex) {
////                        if (!isFirst) {
//                            vm.updatePrice(
//                                st.checks[selectedIndexSelf].currency_type,
//                                st.allCurrencies[selectedIndex]
//                            )
////                        }
////                        isFirst = false
//                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .shadow(8.dp, shape = RoundedCornerShape(25.dp))
                            .background(
                                color = Color.White, shape = RoundedCornerShape(24.dp)
                            )
                            .padding(top = 8.dp, end = 8.dp, bottom = 8.dp, start = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text(
                            "Конвертировать валюту",
                            fontSize = 16.sp,
                            color = Color(0xFF686868),
                            textAlign = TextAlign.Center,
                            maxLines = 3,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.padding(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "Выберите счет для списания",
                                    fontSize = 16.sp,
                                    color = Color(0xFF686868),
                                    textAlign = TextAlign.Center,
                                    maxLines = 3,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .wrapContentSize(Alignment.TopStart)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 12.dp)
                                            .clickable(onClick = { expandedSelf = true })
                                            .border(
                                                width = 1.dp,
                                                color = Color.Gray,
                                                shape = RoundedCornerShape(4.dp)
                                            ),
                                        horizontalArrangement = Arrangement.End,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            st.checks[selectedIndexSelf].currency_type,
                                            fontSize = 16.sp,
                                            color = Color(0xFF686868),
                                            textAlign = TextAlign.Center,
                                            maxLines = 3,
                                            modifier = Modifier.fillMaxWidth(0.8f)

                                        )
                                        Icon(
                                            imageVector = Icons.Default.ArrowDropDown,
                                            contentDescription = ""
                                        )
                                    }

                                    DropdownMenu(
                                        expanded = expandedSelf,
                                        onDismissRequest = { expandedSelf = false },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                Color.White
                                            )
                                    ) {
                                        st.checks.forEachIndexed { index, s ->
                                            DropdownMenuItem(onClick = {
                                                selectedIndexSelf = index
                                                expandedSelf = false
                                            }) {
                                                Text(text = s.currency_type)
                                            }
                                            Divider()
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.weight(0.2f))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "Выберите счет для конвертации",
                                    fontSize = 16.sp,
                                    color = Color(0xFF686868),
                                    textAlign = TextAlign.Center,
                                    maxLines = 3,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .wrapContentSize(Alignment.TopStart)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 12.dp)
                                            .clickable(onClick = { expanded = true })
                                            .border(
                                                width = 1.dp,
                                                color = Color.Gray,
                                                shape = RoundedCornerShape(4.dp)
                                            ),
                                        horizontalArrangement = Arrangement.End,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            st.allCurrencies[selectedIndex],
                                            fontSize = 16.sp,
                                            color = Color(0xFF686868),
                                            textAlign = TextAlign.Center,
                                            maxLines = 3,
                                            modifier = Modifier.fillMaxWidth(0.8f)

                                        )
                                        Icon(
                                            imageVector = Icons.Default.ArrowDropDown,
                                            contentDescription = ""
                                        )
                                    }

                                    DropdownMenu(
                                        expanded = expanded,
                                        onDismissRequest = { expanded = false },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                Color.White
                                            )
                                    ) {
                                        st.allCurrencies.forEachIndexed { index, s ->
                                            DropdownMenuItem(onClick = {
                                                selectedIndex = index
                                                expanded = false
                                            }) {
                                                Text(text = s)
                                            }
                                            Divider()
                                        }
                                    }
                                }
                            }


                        }

                        Text(
                            "Курс составляет 1 ${st.curPrice.type_from} = ${st.curPrice.price}  ${st.curPrice.type_to}",
                            fontSize = 16.sp,
                            color = Color(0xFF686868),
                            textAlign = TextAlign.Center,
                            maxLines = 3,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.padding(8.dp))

                        OutlinedTextField(
                            value = sum,
                            onValueChange = { str -> sum = str },
                            label = { Text(text = "Сумма") },
                            placeholder = { Text(text = "Сумма") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number
                            )
                        )

                        Spacer(modifier = Modifier.padding(8.dp))

                        Button(
                            onClick = {
                                if (sum.toInt() <= 0) {
                                    vm.currentState.value =
                                        TradeViewModel.State.Error(IllegalStateException("Сумма должна быть положительной"))
                                } else {
                                    if (st.checks.filter { ch -> ch.currency_type == st.allCurrencies[selectedIndex] }
                                            .isEmpty()) {
                                        vm.currentState.value =
                                            TradeViewModel.State.Error(IllegalStateException("Необходимо сначала открыть счет ${st.allCurrencies[selectedIndex]}"))
                                    } else {
                                        if (sum.toInt() > st.checks[selectedIndexSelf].value) {
                                            vm.currentState.value =
                                                TradeViewModel.State.Error(IllegalStateException("Недостаточно средств для операции на счете ${st.checks[selectedIndexSelf].currency_type}"))
                                        } else {
                                            vm.convertCurrency(
                                                st.checks[selectedIndexSelf].currency_type,
                                                st.allCurrencies[selectedIndex],
                                                sum
                                            )
                                        }
                                    }
                                }
                            }, modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        ) {
                            Text(
                                "Конвертировать",
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()

                            )
                        }
                    }
                }

            }
            is TradeViewModel.State.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = st.e.message ?: "Неизвестная ошибка")
                }
            }
            TradeViewModel.State.Loading -> {
                LoadingView()
            }
        }
    }

    BackHandler(curState.value is TradeViewModel.State.Error) {
        vm.loadChecks()
    }
}