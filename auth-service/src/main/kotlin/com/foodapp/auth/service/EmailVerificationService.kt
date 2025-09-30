package com.foodapp.auth.service

import com.foodapp.auth.client.UserServiceClient
import com.foodapp.common.dto.CreateUserProfileRequest
import com.foodapp.common.dto.UserProfileResponse
import com.foodapp.auth.repository.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import kotlin.random.Random

@Service
class EmailVerificationService(
    private val emailService: EmailService,
    private val userRepository: UserRepository,
    private val userServiceClient: UserServiceClient,
    @Value("\${app.email.otp.expiry-minutes}")
    private val otpExpiryMinutes: Long
) {
    @Transactional
    fun sendVerificationOtp(userId: Int, email: String) {
        val otp = generateOtp()
        val user = userRepository.findById(userId).orElseThrow { NoSuchElementException("User not found") }
        user.verificationOtp = otp
        user.otpExpiryTime = LocalDateTime.now().plusMinutes(otpExpiryMinutes)
        userRepository.save(user)

        emailService.sendOtpEmail(email, otp)
    }

    @Transactional
    fun verifyEmail(userId: Int, otp: String): Boolean {
        val user = userRepository.findById(userId).orElseThrow { NoSuchElementException("User not found") }

        if (user.isVerified) {
            throw IllegalStateException("Email is already verified")
        }

        if (user.verificationOtp != otp) {
            return false
        }

        if (user.otpExpiryTime?.isBefore(LocalDateTime.now()) == true) {
            return false
        }

        // Verify email and create profile
        user.isVerified = true
        user.verificationOtp = null
        user.otpExpiryTime = null
        user.updatedAt = LocalDateTime.now()
        userRepository.save(user)

        // Create default profile in user-service
        try {
            userServiceClient.createProfile(
                CreateUserProfileRequest(
                    authUserId = user.id!!,
                    firstName = user.firstName,
                    lastName = user.lastName,
                    email = user.email,
                    phone = "",  // Phone will be updated later
                    role = "CUSTOMER"
                )
            )
        } catch (e: Exception) {
            // Log error but don't fail the verification
            println("Failed to create user profile: ${e.message}")
        }

        return true
    }

    private fun generateOtp(): String {
        return Random.nextInt(100000, 999999).toString()
    }
}
