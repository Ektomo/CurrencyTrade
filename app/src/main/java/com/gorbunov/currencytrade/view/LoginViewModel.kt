package com.gorbunov.currencytrade.view

import androidx.compose.runtime.MutableState
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
            try {
                state.value = StartPosition.Loading

//            gate.setComponents(userName, pass)
                var registeredUser = gate.registerUser(
                    RegisterRequestBody(
                        userName,
                        firstName,
                        lastName,
                        pass
                    )
                )
                state.value = StartPosition.NotLogin
            } catch (e: Exception) {
                println(e.message)
                state.value = StartPosition.NotLogin
            }

        }
    }

    fun doLogin(state: MutableState<StartPosition>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                //Тут должна быть логика с логином
                state.value = StartPosition.Loading
                gate.setComponents(
//                    "a", "12345678"
                login.value, pass.value
                )
                val user = gate.tempLogin(
//                    "a"
                login.value
                )
                dataStore.saveUserId(user.id.toString())
                if (user.is_superuser) {
                    state.value = StartPosition.LoginAdmin
                } else {
                    state.value = StartPosition.LoginUser
                }
            } catch (e: Exception) {
                state.value = StartPosition.NotLogin
            }

        }
    }

}