package com.foodapp.auth.service

import com.foodapp.auth.client.UserServiceClient
import com.foodapp.auth.dto.*
import com.foodapp.auth.entity.User
import com.foodapp.auth.repository.UserRepository
import com.foodapp.auth.validator.RequestValidator
import com.foodapp.common.dto.CreateUserProfileRequest
import com.foodapp.common.dto.UserProfileResponse
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
open class AuthService(
    private val userRepository: UserRepository,
    private val emailService: EmailService,
    private val otpService: OtpService,
    private val userServiceClient: UserServiceClient
) {
    @Transactional
    open fun signup(request: SignupRequest): SignupResponse {
        // Validate request
        RequestValidator.validateSignupRequest(request)

        // Check if user already exists
        val enc = BCryptPasswordEncoder()
        if (userRepository.findByEmail(request.email) != null) {
            throw IllegalArgumentException("Email already registered")
        }

        // Create new user with unverified status
        val user = User(
            firstName = request.firstName,
            lastName = request.lastName,
            email = request.email,
            passwordHash = enc.encode(request.password),
            role = request.role,
            isVerified = false
        )

        // Generate and save OTP
        val otp = otpService.generateOtp()
        user.verificationOtp = otp
        user.otpExpiryTime = otpService.getOtpExpiryTime()

        // Save user
        userRepository.save(user)

        // Send OTP email
        emailService.sendOtpEmail(user.email, otp)

        return SignupResponse(
            message = "Signup successful. Please verify your email with the OTP sent.",
            email = user.email
        )
    }

    @Transactional
    open fun verifyEmail(request: VerifyOtpRequest): VerifyOtpResponse {
        val user = userRepository.findByEmail(request.email)
            ?: throw IllegalArgumentException("User not found")

        if (user.isVerified) {
            return VerifyOtpResponse("Email already verified", true)
        }

        if (otpService.isOtpValid(user.verificationOtp, request.otp, user.otpExpiryTime)) {
            user.isVerified = true
            user.verificationOtp = null
            user.otpExpiryTime = null
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
                        role = user.role.name
                    )
                )
            } catch (e: Exception) {
                // Log error but don't fail the verification
                println("Failed to create user profile: ${e.message}")
            }

            return VerifyOtpResponse("Email verified successfully", true)
        }

        return VerifyOtpResponse("Invalid or expired OTP", false)
    }

    @Transactional
    fun resendOtp(email: String): SignupResponse {
        val user = userRepository.findByEmail(email)
            ?: throw IllegalArgumentException("User not found")

        if (user.isVerified) {
            throw IllegalStateException("Email already verified")
        }

        // Generate new OTP
        val otp = otpService.generateOtp()
        user.verificationOtp = otp
        user.otpExpiryTime = otpService.getOtpExpiryTime()
        userRepository.save(user)

        // Send new OTP email
        emailService.sendOtpEmail(user.email, otp)

        return SignupResponse(
            message = "New OTP has been sent to your email",
            email = user.email
        )
    }
}
