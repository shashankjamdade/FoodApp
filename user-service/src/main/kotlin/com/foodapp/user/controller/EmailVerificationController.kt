package com.foodapp.user.controller

import com.foodapp.common.dto.EmailVerificationRequest
import com.foodapp.common.dto.EmailVerificationResponse
import com.foodapp.common.dto.VerifyOtpRequest
import com.foodapp.user.service.EmailVerificationService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/email-verification")
class EmailVerificationController(
    private val emailVerificationService: EmailVerificationService
) {

    @PostMapping("/send-otp")
    fun sendVerificationOtp(@RequestBody request: EmailVerificationRequest): EmailVerificationResponse {
        emailVerificationService.sendVerificationOtp(request.email)
        return EmailVerificationResponse(
            message = "OTP sent successfully",
            verified = false
        )
    }

    @PostMapping("/verify")
    fun verifyEmail(@RequestBody request: VerifyOtpRequest): EmailVerificationResponse {
        val verified = emailVerificationService.verifyEmail(request.email, request.otp)
        return EmailVerificationResponse(
            message = if (verified) "Email verified successfully" else "Invalid or expired OTP",
            verified = verified
        )
    }

    @GetMapping("/status")
    fun checkVerificationStatus(@RequestParam email: String): EmailVerificationResponse {
        val verified = emailVerificationService.isEmailVerified(email)
        return EmailVerificationResponse(
            message = if (verified) "Email is verified" else "Email is not verified",
            verified = verified
        )
    }
}
