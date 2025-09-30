package com.foodapp.common.dto

import java.time.LocalDateTime
import java.util.*

data class CreateUserProfileRequest(
    val authUserId: Int,
    val firstName: String,
    val lastName: String?,
    val email: String,
    val phone: String = "",
    val role: String,
    val profilePicture: String? = null
)

data class UserProfileResponse(
    val userId: UUID,
    val authUserId: Int,
    val firstName: String,
    val lastName: String?,
    val email: String,
    val phone: String,
    val role: String,
    val profilePicture: String?,
    val isDefault: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
