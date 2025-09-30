package com.foodapp.user.service

import com.foodapp.user.domain.model.EmailVerification
import com.foodapp.user.repository.EmailVerificationRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import kotlin.random.Random

@Service
open class EmailVerificationService(
    private val emailVerificationRepository: EmailVerificationRepository,
    private val emailService: EmailService,
    @Value("\${app.otp.expiration-minutes:5}")
    private val otpExpirationMinutes: Long
) {

    @Transactional
    open fun sendVerificationOtp(email: String) {
        val otp = generateOtp()
        val verification = EmailVerification(
            email = email,
            otp = otp,
            expiresAt = LocalDateTime.now().plusMinutes(otpExpirationMinutes)
        )

        emailVerificationRepository.save(verification)
        emailService.sendOtpEmail(email, otp)
    }

    @Transactional
    open fun verifyEmail(email: String, otp: String): Boolean {
        val verification = emailVerificationRepository
            .findByEmailAndVerifiedFalseOrderByCreatedAtDesc(email)
            ?: return false

        if (verification.expiresAt.isBefore(LocalDateTime.now())) {
            return false
        }

        if (verification.otp != otp) {
            return false
        }

        verification.verified = true
        emailVerificationRepository.save(verification)
        return true
    }

    open fun isEmailVerified(email: String): Boolean {
        return emailVerificationRepository.existsByEmailAndVerifiedTrue(email)
    }

    private fun generateOtp(): String {
        return Random.nextInt(100000, 999999).toString()
    }
}
