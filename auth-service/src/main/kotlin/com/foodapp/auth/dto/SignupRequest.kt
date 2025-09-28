package com.foodapp.auth.dto

import com.foodapp.auth.entity.Role

data class SignupRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val role: Role
)

data class VerifyOtpRequest(
    val email: String,
    val otp: String
)

data class SignupResponse(
    val message: String,
    val email: String,
    val requiresVerification: Boolean = true
)

data class VerifyOtpResponse(
    val message: String,
    val verified: Boolean
)
