package com.gorbunov.currencytrade.model

@kotlinx.serialization.Serializable
data class AdminUserResponse(
    val id: Long,
    val username: String,
    val updated_at: String,
    val is_approved: Boolean,
    val is_active: Boolean
)