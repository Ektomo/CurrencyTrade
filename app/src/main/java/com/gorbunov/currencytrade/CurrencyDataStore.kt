package com.gorbunov.currencytrade

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CurrencyDataStore @Inject constructor(private val context: Context) {


    val getUserId: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_ID_KEY] ?: ""
        }

    suspend fun saveUserId(name: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = name
        }
    }



    companion object{
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("currencyTrader")
        val USER_ID_KEY = stringPreferencesKey("user_id")
    }



}