package com.foodapp.auth.entity

import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor
import org.jetbrains.annotations.NotNull
import java.time.LocalDateTime

@Entity
@Table(name = "appusers")
@AllArgsConstructor
@NoArgsConstructor
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @Column(nullable = false)
    var firstName: String = "",

    @Column(nullable = true)
    var lastName: String? = null,

    @NotNull
    @Column(nullable = false, unique = true)
    var email: String = "",

    @Column(nullable = false)
    var passwordHash: String = "",

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var role: Role = Role.CUSTOMER,

    @Column(nullable = false)
    var isVerified: Boolean = false,

    @Column(length = 6)
    var verificationOtp: String? = null,

    @Column
    var otpExpiryTime: LocalDateTime? = null,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
)
