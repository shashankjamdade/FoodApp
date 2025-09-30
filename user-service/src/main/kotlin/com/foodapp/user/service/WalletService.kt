package com.foodapp.user.service

import com.foodapp.user.domain.model.*
import com.foodapp.user.repository.WalletRepository
import com.foodapp.user.repository.WalletTransactionRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@Service
open class WalletService(
    private val walletRepository: WalletRepository,
    private val walletTransactionRepository: WalletTransactionRepository
) {
    companion object {
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
    }

    fun getWallet(userId: UUID): WalletDto {
        return walletRepository.findByUserProfileUserId(userId)
            ?.toDto()
            ?: throw NoSuchElementException("Wallet not found")
    }

    fun getTransactions(userId: UUID, pageable: Pageable): Page<WalletTransactionDto> {
        return walletTransactionRepository.findByWalletUserProfileUserId(userId, pageable)
            .map { it.toDto() }
    }

    @Transactional
    fun createWallet(userProfile: UserProfile): WalletDto {
        if (walletRepository.findByUserProfileUserId(userProfile.userId) != null) {
            throw IllegalStateException("Wallet already exists for user")
        }

        val wallet = Wallet(userProfile = userProfile)
        return walletRepository.save(wallet).toDto()
    }

    @Transactional
    fun addTransaction(userId: UUID, request: CreateTransactionRequest): WalletTransactionDto {
        val wallet = walletRepository.findByUserProfileUserId(userId)
            ?: throw NoSuchElementException("Wallet not found")

        val transactionType = try {
            TransactionType.valueOf(request.type.uppercase())
        } catch (_: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid transaction type. Must be either CREDIT or DEBIT")
        }

        // Validate transaction
        when (transactionType) {
            TransactionType.DEBIT -> {
                if (wallet.balance < request.amount) {
                    throw IllegalStateException("Insufficient balance")
                }
                wallet.balance = wallet.balance.subtract(request.amount)
            }
            TransactionType.CREDIT -> {
                wallet.balance = wallet.balance.add(request.amount)
            }
        }

        wallet.updatedAt = LocalDateTime.now()
        walletRepository.save(wallet)

        val transaction = WalletTransaction(
            wallet = wallet,
            amount = request.amount,
            type = transactionType,
            description = request.description,
            referenceId = request.referenceId
        )

        return walletTransactionRepository.save(transaction).toDto()
    }

    private fun Wallet.toDto() = WalletDto(
        id = id,
        balance = balance,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    private fun WalletTransaction.toDto() = WalletTransactionDto(
        id = id,
        amount = amount,
        type = type.name,
        description = description,
        referenceId = referenceId,
        createdAt = createdAt
    )
}
