package com.gorbunov.currencytrade.view.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gorbunov.currencytrade.CurrencyDataStore
import com.gorbunov.currencytrade.gate.Connection
import com.gorbunov.currencytrade.model.UnapprovedUserResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject




@HiltViewModel
class AdminApprovedListViewModel @Inject constructor(
    private val dataStore: CurrencyDataStore,
    private val gate: Connection
): ViewModel(){

    val curState: MutableStateFlow<State> = MutableStateFlow(State.Loading)

    init {
        loadList()
    }

    sealed class State() {
        object Loading : State()
        data class Error(val e: Exception) : State()
        data class Data(val data: List<UnapprovedUserResponse>) : State()
    }

    fun loadList(){
        viewModelScope.launch(Dispatchers.IO) {
            curState.value = State.Loading
            val result = gate.getUnapprovedList()
            curState.value = State.Data(result)
        }
    }

}