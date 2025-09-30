package com.foodapp.user.controller

import com.foodapp.user.service.WalletService.Companion.CreateTransactionRequest
import com.foodapp.user.service.WalletService.Companion.WalletDto
import com.foodapp.user.service.WalletService.Companion.WalletTransactionDto
import com.foodapp.user.service.WalletService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/users/{userId}/wallet")
class WalletController(
    private val walletService: WalletService
) {
    @GetMapping
    fun getWallet(@PathVariable userId: UUID): WalletDto {
        return walletService.getWallet(userId)
    }

    @GetMapping("/transactions")
    fun getTransactions(
        @PathVariable userId: UUID,
        pageable: Pageable
    ): Page<WalletTransactionDto> {
        return walletService.getTransactions(userId, pageable)
    }

    @PostMapping("/transactions")
    @ResponseStatus(HttpStatus.CREATED)
    fun addTransaction(
        @PathVariable userId: UUID,
        @RequestBody request: CreateTransactionRequest
    ): WalletTransactionDto {
        return walletService.addTransaction(userId, request)
    }
}
