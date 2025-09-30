package com.foodapp.common.dto

data class EmailVerificationRequest(
    val email: String
)

data class VerifyOtpRequest(
    val email: String,
    val otp: String
)

data class EmailVerificationResponse(
    val message: String,
    val verified: Boolean
)
