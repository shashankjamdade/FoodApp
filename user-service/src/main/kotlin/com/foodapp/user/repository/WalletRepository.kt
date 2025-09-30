package com.foodapp.user.repository

import com.foodapp.user.domain.model.Wallet
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface WalletRepository : JpaRepository<Wallet, UUID> {
    fun findByUserProfileUserId(userId: UUID): Wallet?
}
