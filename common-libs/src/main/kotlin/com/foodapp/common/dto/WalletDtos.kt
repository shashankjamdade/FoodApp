package com.foodapp.common.dto

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

data class WalletDto(
    val id: UUID,
    val balance: BigDecimal,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

data class WalletTransactionDto(
    val id: UUID,
    val amount: BigDecimal,
    val type: String,
    val description: String,
    val referenceId: String?,
    val createdAt: LocalDateTime
)

data class CreateTransactionRequest(
    val amount: BigDecimal,
    val type: String,
    val description: String,
    val referenceId: String? = null
)
