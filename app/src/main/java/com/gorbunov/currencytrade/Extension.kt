package com.gorbunov.currencytrade

fun Boolean.toYes(): String = if (this) "Да" else "Нет"
fun Boolean.toNo(): String = if (this) "Нет" else "Да"