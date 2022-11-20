package com.gorbunov.currencytrade.view.user.history

import androidx.lifecycle.ViewModel
import com.gorbunov.currencytrade.gate.Connection
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryCurrencyViewModel @Inject constructor(
    val gate: Connection
): ViewModel() {
}