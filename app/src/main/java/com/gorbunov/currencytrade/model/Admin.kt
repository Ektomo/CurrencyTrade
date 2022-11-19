package com.gorbunov.currencytrade.model

@kotlinx.serialization.Serializable
data class UnapprovedUserResponse(
    val id: Long,
    val username: String,
    val updated_at: String,
    val is_approved: Boolean
)