package com.foodapp.user.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.util.*

@Entity
@Table(name = "user_profile", schema = "core")
data class UserProfile(
    @Id var userId: UUID? = null,
    var fullName: String? = null,
    var phone: String? = null,
    var walletBalance: BigDecimal = BigDecimal.ZERO
)
