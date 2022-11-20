package com.gorbunov.currencytrade.view.user.bag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gorbunov.currencytrade.gate.Connection
import com.gorbunov.currencytrade.model.Check
import com.gorbunov.currencytrade.model.User
import com.gorbunov.currencytrade.view.user.profile.ProfileViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BagViewModel @Inject constructor(
    val gate: Connection
): ViewModel() {


    init {
        loadChecks()
    }

    val currentState: MutableStateFlow<State> = MutableStateFlow(State.Loading)

    fun loadChecks(){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                currentState.value = State.Loading
                val checks = gate.getChecks()
                currentState.value = State.Data(checks)
            }catch (e: Exception){
                currentState.value = State.Error(e)
            }


        }
    }

    sealed class State {
        object Loading : State()
        data class Error(val e: Exception) : State()
        data class Data(val data: List<Check>) : State()
    }
}