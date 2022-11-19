package com.gorbunov.currencytrade.view.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gorbunov.currencytrade.CurrencyDataStore
import com.gorbunov.currencytrade.gate.Connection
import com.gorbunov.currencytrade.model.AdminUserResponse
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

    sealed class State {
        object Loading : State()
        data class Error(val e: Exception) : State()
        data class Data(val data: List<AdminUserResponse>) : State()
    }

    private fun loadList(){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                curState.value = State.Loading
                val result = gate.getUnapprovedList()
                curState.value = State.Data(result)
            }catch (e: Exception){
                if (e.message?.contains("Not found", ignoreCase = true) == true){
                    curState.value = State.Error(RuntimeException("Нет заявок на подтверждение"))
                }else{
                    curState.value = State.Error(e)
                }
            }
        }
    }

    fun approveUserBy(id: Long){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                curState.value = State.Loading
                gate.approveUser(id)
                val result = gate.getUnapprovedList()
                curState.value = State.Data(result)
            }catch (e: Exception){
                if (e.message?.contains("Not found", ignoreCase = true) == true){
                    curState.value = State.Error(RuntimeException("Нет заявок на подтверждение"))
                }else{
                    curState.value = State.Error(e)
                }
            }
        }
    }

}