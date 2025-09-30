package com.foodapp.common.dto

import java.time.LocalDateTime
import java.util.*

data class AddressDto(
    val id: UUID,
    val addressLine1: String,
    val addressLine2: String?,
    val city: String,
    val state: String,
    val postalCode: String,
    val country: String,
    val landmark: String?,
    val addressType: String,
    val isDefault: Boolean
)

data class CreateAddressRequest(
    val addressLine1: String,
    val addressLine2: String?,
    val city: String,
    val state: String,
    val postalCode: String,
    val country: String = "India",
    val landmark: String?,
    val addressType: String,
    val isDefault: Boolean = false
)

data class UpdateAddressRequest(
    val addressLine1: String?,
    val addressLine2: String?,
    val city: String?,
    val state: String?,
    val postalCode: String?,
    val country: String?,
    val landmark: String?,
    val addressType: String?,
    val isDefault: Boolean?
)
