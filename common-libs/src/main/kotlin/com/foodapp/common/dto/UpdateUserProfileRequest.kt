package com.foodapp.common.dto

data class UpdateUserProfileRequest(
    val firstName: String?,
    val lastName: String?,
    val phone: String?,
    val role: String?,
    val profilePicture: String?,
    val isDefault: Boolean?
)
