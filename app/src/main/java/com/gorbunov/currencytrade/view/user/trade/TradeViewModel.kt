package com.gorbunov.currencytrade.view.user.trade

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gorbunov.currencytrade.gate.Connection
import com.gorbunov.currencytrade.model.Check
import com.gorbunov.currencytrade.model.CurrencyPrice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TradeViewModel @Inject constructor(
    val gate: Connection
) : ViewModel() {

    val currentState: MutableStateFlow<State> = MutableStateFlow(State.Loading)
    var curs = listOf("RUB", "USD", "EUR", "AED", "AFN", "ALL", "AMD", "ANG",
        "AOA", "ARS", "AUD", "AWG", "AZN", "BAM", "BBD")

    init {
        loadChecks()
    }

    fun loadChecks() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                currentState.value = State.Loading
                if (curs.isEmpty()) {
                    curs = gate.getCurrencies()
                }
                val checks = gate.getChecks()
//                val allCurs = listOf("RUB", "USD", "EUR", "AED", "AFN", "ALL", "AMD", "ANG",
//                    "AOA", "ARS", "AUD", "AWG", "AZN", "BAM", "BBD")
                val curPrice = gate.getCurrencyValue("RUB", curs.first())
                currentState.value = State.Data(checks, curs, CurrencyPrice(
                    "RUB", "AUD", 0.62f
                ))
            } catch (e: Exception) {
                currentState.value = State.Error(e)
            }
        }
    }

    fun updatePrice(from: String, to: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val d = currentState.value as State.Data
                currentState.value = State.Loading
                val curPrice = gate.getCurrencyValue(from, to)
                currentState.value = d.copy(curPrice = curPrice)
            } catch (e: Exception) {
                currentState.value = State.Error(e)
            }
        }
    }

    fun convertCurrency(from: String, to: String, value: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val d = currentState.value as State.Data
                currentState.value = State.Loading
                gate.convert(from, to, value)

                if (curs.isEmpty()) {
                    curs = gate.getCurrencies()
                }
                val checks = gate.getChecks()
                currentState.value = d.copy(checks = checks, allCurrencies = curs)
            } catch (e: Exception) {
                currentState.value = State.Error(e)
            }
        }
    }


    sealed class State {
        object Loading : State()
        data class Error(val e: Exception) : State()
        data class Data(val checks: List<Check>, val allCurrencies: List<String>, val curPrice: CurrencyPrice) : State()
    }

}