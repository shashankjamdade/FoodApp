package com.foodapp.user.domain.model

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "wallet_transactions")
open class WalletTransaction {
    @Id
    @get:Column(name = "id")
    open var id: UUID = UUID.randomUUID()

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", nullable = false)
    open lateinit var wallet: Wallet

    @get:Column(nullable = false)
    open var amount: BigDecimal = BigDecimal.ZERO

    @get:Enumerated(EnumType.STRING)
    @get:Column(nullable = false)
    open var type: TransactionType = TransactionType.CREDIT

    @get:Column(nullable = false)
    open var description: String = ""

    @get:Column(name = "reference_id", nullable = true)
    open var referenceId: String? = null

    @get:Column(name = "created_at")
    open var createdAt: LocalDateTime = LocalDateTime.now()

    constructor()

    constructor(
        wallet: Wallet,
        amount: BigDecimal,
        type: TransactionType,
        description: String,
        referenceId: String? = null
    ) {
        this.wallet = wallet
        this.amount = amount
        this.type = type
        this.description = description
        this.referenceId = referenceId
    }
}

enum class TransactionType {
    CREDIT,
    DEBIT
}
