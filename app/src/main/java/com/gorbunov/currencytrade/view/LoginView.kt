package com.gorbunov.currencytrade.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gorbunov.currencytrade.StartPosition
import kotlinx.coroutines.flow.asStateFlow

@Composable
fun LoginView(loginViewModel: LoginViewModel, startPosition: MutableState<StartPosition>) {

    val user = loginViewModel.login.collectAsState()
    val password = loginViewModel.pass.collectAsState()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize(),
//        contentAlignment = Alignment.BottomCenter
    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(top = 30.dp)
//                .background(Color.White),
//            contentAlignment = Alignment.TopCenter
//        ) {
//            Image(painterResource(R.drawable.logo), contentDescription = "logo")
//
//        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .background(Color.White)
                .padding(10.dp)
                .verticalScroll(rememberScrollState()),
        ) {



            Text(
                text = "Авторизация",
                style = TextStyle(fontWeight = FontWeight.Bold, letterSpacing = 2.sp),
                fontSize = 30.sp
            )
            Spacer(modifier = Modifier.padding(20.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                OutlinedTextField(
                    value = user.value,
                    onValueChange = { loginViewModel.login.value = it },
                    label = { Text(text = "Логин") },
                    placeholder = { Text(text = "Логин") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.8f),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    })
                )
                OutlinedTextField(
                    value = password.value,
                    onValueChange = { loginViewModel.pass.value = it },
                    label = { Text(text = "Пароль") },
                    placeholder = { Text(text = "Пароль") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.8f),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Password
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        loginViewModel.doLogin(startPosition)
                    })
                )
                Spacer(Modifier.padding(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(onClick = {
                        loginViewModel.doLogin(startPosition)
                    }) {
                        Text(text = "Войти")
                    }
                    Spacer(modifier = Modifier.padding(4.dp))
                    Button(onClick = {
                        startPosition.value = StartPosition.NeedRegister
                        }) {
                        Text(text = "Регистрация")
                    }

                }
//                Image(
//                    painter = painterResource(id = R.drawable.logo),
//                    contentDescription = "logo",
//                    alignment = BiasAlignment(0f, -1f),
//                )
            }

        }
    }
}

@Composable
fun RegisterView(loginViewModel: LoginViewModel, startPosition: MutableState<StartPosition>) {

    var user by remember{
        mutableStateOf("")
    }
    var password by remember{
        mutableStateOf("")
    }
    var firstName by remember{
        mutableStateOf("")
    }
    var lastName by remember{
        mutableStateOf("")
    }
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .background(Color.White)
                .padding(10.dp)
                .verticalScroll(rememberScrollState()),
        ) {

            Text(
                text = "Регистрация",
                style = TextStyle(fontWeight = FontWeight.Bold, letterSpacing = 2.sp),
                fontSize = 30.sp
            )
            Spacer(modifier = Modifier.padding(20.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                OutlinedTextField(
                    value = user,
                    onValueChange = { user = it },
                    label = { Text(text = "Логин") },
                    placeholder = { Text(text = "Логин") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.8f),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    })
                )

                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text(text = "Имя") },
                    placeholder = { Text(text = "Имя") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.8f),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    })
                )
                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text(text = "Фамилия") },
                    placeholder = { Text(text = "Фамилия") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.8f),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    })
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(text = "Пароль") },
                    placeholder = { Text(text = "Пароль") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.8f),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Password
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        loginViewModel.doRegister(
                            userName = user,
                            pass = password,
                            firstName = firstName,
                            lastName = lastName,
                            state = startPosition
                        )
                    })
                )
                Spacer(Modifier.padding(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(onClick = {
                        loginViewModel.doRegister(
                            userName = user,
                            pass = password,
                            firstName = firstName,
                            lastName = lastName,
                            state = startPosition
                        )
                    }) {
                        Text(text = "Регистрация")
                    }

                }


//                Image(
//                    painter = painterResource(id = R.drawable.logo),
//                    contentDescription = "logo",
//                    alignment = BiasAlignment(0f, -1f),
//                )
            }

        }
    }
}