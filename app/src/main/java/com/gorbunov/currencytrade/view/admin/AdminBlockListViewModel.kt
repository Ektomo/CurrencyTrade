package com.gorbunov.currencytrade.view.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gorbunov.currencytrade.gate.Connection
import com.gorbunov.currencytrade.model.AdminUserResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminBlockListViewModel @Inject constructor(
    private val gate: Connection
): ViewModel() {
    val curState: MutableStateFlow<State> = MutableStateFlow(State.Loading)

    init {
        loadList()
    }

    sealed class State {
        object Loading : State()
        data class Error(val e: Exception) : State()
        data class Data(val data: List<AdminUserResponse>) : State()
    }

    fun loadList(){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                curState.value = State.Loading
                val result = gate.getApprovedList()
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

    fun blockUserBy(id: Long){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                curState.value = State.Loading
                gate.changeBlockStatusBy(id)
                val result = gate.getApprovedList()
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