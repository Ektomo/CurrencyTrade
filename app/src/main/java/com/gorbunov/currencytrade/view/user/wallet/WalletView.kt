package com.gorbunov.currencytrade.view.user.wallet

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.gorbunov.currencytrade.view.LoadingView
import kotlin.math.absoluteValue

@Composable
fun WalletView(vm: WalletViewModel, navController: NavHostController) {

    val curState = vm.currentState.collectAsState()

    Crossfade(targetState = curState) {
        when (val st = it.value) {
            is WalletViewModel.State.Data ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(bottom = 40.dp)
                ) {
                    item {
                        Text(
                            text = "Основной счет",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, top = 16.dp, end = 16.dp),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                    }
                    item {
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
                                "Сумма счета в рублях: ${st.rudCheck.value}",
                                fontSize = 16.sp,
                                color = Color(0xFF686868),
                                textAlign = TextAlign.Center,
                                maxLines = 3,
                                modifier = Modifier
                                    .fillMaxWidth(0.6f)
                                    .padding(vertical = 12.dp)
                            )
                            var checkNumber by remember {
                                mutableStateOf("0")
                            }
                            OutlinedTextField(
                                value = checkNumber,
                                onValueChange = { str -> checkNumber = str },
                                label = { Text(text = "Сумма для изменения") },
                                placeholder = { Text(text = "Сумма для изменения") },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth(0.8f)
                                    .padding(bottom = 8.dp),
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Number
                                )
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Button(onClick = {
                                    val sum = 0 - checkNumber.toInt()
                                    if (sum < 0 && sum.absoluteValue > st.rudCheck.value) {
                                        vm.currentState.value =
                                            WalletViewModel.State.Error(IllegalStateException("Не хватает средств для вывода"))
                                    } else {
                                        vm.fillWallet(0 - checkNumber.toInt())
                                    }
                                }, modifier = Modifier.wrapContentWidth()) {
                                    Text(
                                        "Снять",
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Start,
                                        modifier = Modifier.wrapContentWidth()
                                    )
                                }
                                Button(
                                    onClick = { vm.fillWallet(checkNumber.toInt()) },
                                    modifier = Modifier.wrapContentWidth()
                                ) {
                                    Text(
                                        "Пополнить",
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Start,
                                        modifier = Modifier.wrapContentWidth()
                                    )
                                }
                            }


                        }
                    }

                    item {
                        var expanded by remember { mutableStateOf(false) }
                        var selectedIndex by remember {
                            mutableStateOf(0)
                        }
                        val items = st.currencies
                        Column(
                            modifier = Modifier
                                .padding(start = 16.dp, top = 16.dp, end = 16.dp)
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
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Text(
                                    "Выберите валюту",
                                    fontSize = 16.sp,
                                    color = Color(0xFF686868),
                                    textAlign = TextAlign.Center,
                                    maxLines = 3,
                                    modifier = Modifier.fillMaxWidth(0.6f)
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
                                            items[selectedIndex],
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
                                        items.forEachIndexed { index, s ->
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

                            Spacer(modifier = Modifier.padding(8.dp))

                            Button(onClick = {
                                if (st.data.any { ch -> ch.currency_type == items[selectedIndex] }) {
                                    vm.currentState.value =
                                        WalletViewModel.State.Error(IllegalStateException("Счет данной валюты уже существует"))
                                } else {
                                    vm.createCheck(items[selectedIndex])
                                }
                            }, modifier = Modifier.wrapContentWidth()) {
                                Text(
                                    "Открыть новый счет",
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Start,
                                    modifier = Modifier.wrapContentWidth()
                                )
                            }

                        }
                    }


                    item {
                        Spacer(modifier = Modifier.padding(12.dp))
                        Text(
                            text = "Дополнительные счета",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, top = 16.dp, end = 16.dp),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                    }

                    st.data.forEach { ch ->
                        item {
                            Row(modifier = Modifier
                                .padding(start = 16.dp, top = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .shadow(8.dp, shape = RoundedCornerShape(25.dp))
                                .background(
                                    color = Color.White, shape = RoundedCornerShape(24.dp)
                                )
                                .clickable {
                                    navController.navigate("check_detail") {
                                        launchSingleTop = true
                                    }
                                }
                                .padding(top = 8.dp, end = 8.dp, bottom = 8.dp, start = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(
                                    "Валюта ${ch.currency_type}в количестве: ${ch.value}",
                                    fontSize = 16.sp,
                                    color = Color(0xFF686868),
                                    textAlign = TextAlign.Center,
                                    maxLines = 3,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 16.dp)
                                )

                            }
                        }
                    }

                    item{
                        Spacer(modifier = Modifier.padding(30.dp))
                    }

                }
            is WalletViewModel.State.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = st.e.message ?: "Неизвестная ошибка")
                }
            }
            WalletViewModel.State.Loading -> {
                LoadingView()
            }
        }
    }

    BackHandler(curState.value is WalletViewModel.State.Error) {
        vm.loadChecks()
    }
}

