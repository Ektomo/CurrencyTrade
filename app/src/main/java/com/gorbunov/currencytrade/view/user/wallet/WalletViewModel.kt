package com.gorbunov.currencytrade.view.user.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gorbunov.currencytrade.gate.Connection
import com.gorbunov.currencytrade.model.Check
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    val gate: Connection
): ViewModel() {

    var rubCheck = Check(0f, "", -1, "")
    var clearChecks = listOf<Check>()
    var curs = listOf("RUB", "USD", "EUR", "AED", "AFN", "ALL", "AMD", "ANG",
        "AOA", "ARS", "AUD", "AWG", "AZN", "BAM", "BBD")

    init {
        loadChecks()
    }

    val currentState: MutableStateFlow<State> = MutableStateFlow(State.Loading)

    fun createCheck(cur: String){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                currentState.value = State.Loading
                gate.createCheckWith(cur)
                val checks = gate.getChecks()
                rubCheck = checks.first { it.currency_type == "RUB" }
                clearChecks = checks.filter { it.currency_type != "RUB" }
                currentState.value = State.Data(rubCheck, clearChecks, curs)
            }catch (e: Exception){
                currentState.value = State.Error(e)
            }
        }
    }

    fun fillWallet(num: Int){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                currentState.value = State.Loading
                gate.fillCheckWith(num)
                val checks = gate.getChecks()
                rubCheck = checks.first { it.currency_type == "RUB" }
                clearChecks = checks.filter { it.currency_type != "RUB" }
                currentState.value = State.Data(rubCheck, clearChecks, curs)
            }catch (e: Exception){
                currentState.value = State.Error(e)
            }
        }
    }

    fun loadChecks(){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                currentState.value = State.Loading
                if (curs.isEmpty()){
                    curs = gate.getCurrencies()
                }
                val checks = gate.getChecks()
                rubCheck = checks.first { it.currency_type == "RUB" }
                clearChecks = checks.filter { it.currency_type != "RUB" }
                currentState.value = State.Data(rubCheck, clearChecks, curs)
            }catch (e: Exception){
                currentState.value = State.Error(e)
            }
        }
    }

    sealed class State {
        object Loading : State()
        data class Error(val e: Exception) : State()
        data class Data(val rudCheck: Check, val data: List<Check>, val currencies: List<String>) : State()
    }
}