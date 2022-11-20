package com.gorbunov.currencytrade.view.user.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gorbunov.currencytrade.CurrencyDataStore
import com.gorbunov.currencytrade.gate.Connection
import com.gorbunov.currencytrade.model.AdminUserResponse
import com.gorbunov.currencytrade.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val dataStore: CurrencyDataStore,
    private val gate: Connection
): ViewModel() {

    val currentState: MutableStateFlow<State> = MutableStateFlow(State.Loading)

    init {
        loadInfo()
    }

    fun loadInfo(){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                currentState.value = State.Loading
                val user = gate.getSelf()

                currentState.value = State.Data(user)
            }catch (e: Exception){
                currentState.value = State.Error(e)
            }
        }

    }


    sealed class State {
        object Loading : State()
        data class Error(val e: Exception) : State()
        data class Data(val data: User) : State()
    }


}

