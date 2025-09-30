package com.foodapp.user.repository

import com.foodapp.user.domain.model.WalletTransaction
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface WalletTransactionRepository : JpaRepository<WalletTransaction, UUID> {
    fun findByWalletId(walletId: UUID, pageable: Pageable): Page<WalletTransaction>
    fun findByWalletUserProfileUserId(userId: UUID, pageable: Pageable): Page<WalletTransaction>
}
