package com.gorbunov.currencytrade.model

@kotlinx.serialization.Serializable
data class User(
    val id: Long,
    val username: String,
    val first_name: String,
    val last_name: String,
    val is_active: Boolean,
    val is_approved: Boolean,
    val is_superuser: Boolean,
    val created_at: String,
    val updated_at: String,
    val full_name: String
)


@kotlinx.serialization.Serializable
data class RegisterRequestBody(
    val username: String,
    val first_name: String,
    val last_name: String,
    val password: String,
)

//2022-11-19T13:50:04.219Z

@kotlinx.serialization.Serializable
data class UserResponseBody(
    val id: Long,
    val username: String,
    val first_name: String,
    val last_name: String,
    val is_active: Boolean,
    val is_approved: Boolean,
    val created_at: String,
    val updated_at: String,
    val full_name: String
)

@kotlinx.serialization.Serializable
data class UserLoginResponseBody(
    val id: Long,
    val username: String,
    val first_name: String,
    val last_name: String,
    val is_active: Boolean,
    val is_approved: Boolean,
    val created_at: String,
    val updated_at: String,
    val full_name: String,
    val is_superuser: Boolean
)

@kotlinx.serialization.Serializable
data class Check(
    val value : Float,
    val currency_type: String,
    val id: Long,
    val created_at: String
)

@kotlinx.serialization.Serializable
data class CurrencyTypes(
//    val success: Boolean,
    val currencies: List<String>
)

@kotlinx.serialization.Serializable
data class CurrencyPrice(
    val type_from: String,
    val type_to: String,
    val price: Float
)


@kotlinx.serialization.Serializable
data class ChecksHistory(
    val currency_type_from: String,
    val currency_type_to: String,
    val value_to: Float,
    val value_from: Float
)

