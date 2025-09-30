package com.foodapp.user.domain.model

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "wallets")
open class Wallet {
    @Id
    @get:Column(name = "id")
    open var id: UUID = UUID.randomUUID()

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    open lateinit var userProfile: UserProfile

    @get:Column(nullable = false)
    open var balance: BigDecimal = BigDecimal.ZERO

    @OneToMany(mappedBy = "wallet", cascade = [CascadeType.ALL], orphanRemoval = true)
    open var transactions: MutableList<WalletTransaction> = mutableListOf()

    @get:Column(name = "created_at")
    open var createdAt: LocalDateTime = LocalDateTime.now()

    @get:Column(name = "updated_at")
    open var updatedAt: LocalDateTime = LocalDateTime.now()

    constructor()

    constructor(
        userProfile: UserProfile,
        balance: BigDecimal = BigDecimal.ZERO
    ) {
        this.userProfile = userProfile
        this.balance = balance
    }
}
