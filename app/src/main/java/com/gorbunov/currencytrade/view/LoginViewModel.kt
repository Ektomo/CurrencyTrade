package com.gorbunov.currencytrade.view

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gorbunov.currencytrade.CurrencyDataStore
import com.gorbunov.currencytrade.StartPosition
import com.gorbunov.currencytrade.gate.Connection
import com.gorbunov.currencytrade.model.RegisterRequestBody
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dataStore: CurrencyDataStore,
    private val gate: Connection
) : ViewModel() {

    val login = MutableStateFlow("")
    val pass = MutableStateFlow("")




    fun doRegister(
        userName: String = "user",
        firstName: String = "user",
        lastName: String = "userovskiy",
        pass: String = "123",
        state: MutableState<StartPosition>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            //Тут должна быть логика с логином
            state.value = StartPosition.Loading

//            gate.setComponents(userName, pass)
            var registeredUser  = gate.registerUser(
                RegisterRequestBody(
                    userName,
                    firstName,
                    lastName,
                    pass
                )
            )
            state.value = StartPosition.NotLogin
        }
    }

    fun doLogin(state: MutableState<StartPosition>) {
        viewModelScope.launch(Dispatchers.IO) {
            //Тут должна быть логика с логином
            state.value = StartPosition.Loading
            gate.setComponents(
                "user", "12345678"
//                login.value, pass.value
            )
            val user = gate.tempLogin(
                "user"
//                login.value
            )
            dataStore.saveUserId(user.id.toString())
            state.value = StartPosition.LoginAdmin
        }
    }

}