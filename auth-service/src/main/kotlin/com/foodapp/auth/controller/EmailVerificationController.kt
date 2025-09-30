package com.foodapp.auth.controller

import com.foodapp.auth.service.EmailVerificationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth/email")
class EmailVerificationController(
    private val emailVerificationService: EmailVerificationService
) {
    @PostMapping("/{userId}/send-otp")
    fun sendVerificationOtp(
        @PathVariable userId: Int,
        @RequestParam email: String
    ): ResponseEntity<Map<String, String>> {
        emailVerificationService.sendVerificationOtp(userId, email)
        return ResponseEntity.ok(mapOf("message" to "OTP sent successfully"))
    }

    @PostMapping("/{userId}/verify")
    fun verifyEmail(
        @PathVariable userId: Int,
        @RequestBody otp: String
    ): ResponseEntity<Map<String, Any>> {
        val verified = emailVerificationService.verifyEmail(userId, otp)
        return ResponseEntity.ok(
            mapOf(
                "verified" to verified,
                "message" to if (verified) "Email verified successfully" else "Invalid or expired OTP"
            )
        )
    }
}
