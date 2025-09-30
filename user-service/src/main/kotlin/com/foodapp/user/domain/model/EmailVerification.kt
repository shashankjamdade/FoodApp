package com.foodapp.user.domain.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "email_verifications")
open class EmailVerification {
    @Id
    @get:Column(name = "id")
    open var id: UUID = UUID.randomUUID()

    @get:Column(nullable = false)
    open var email: String = ""

    @get:Column(nullable = false)
    open var otp: String = ""

    @get:Column(nullable = false)
    open var expiresAt: LocalDateTime = LocalDateTime.now()

    @get:Column(nullable = false)
    open var verified: Boolean = false

    @get:Column(name = "created_at")
    open var createdAt: LocalDateTime = LocalDateTime.now()

    constructor()

    constructor(
        email: String,
        otp: String,
        expiresAt: LocalDateTime,
        verified: Boolean = false
    ) {
        this.email = email
        this.otp = otp
        this.expiresAt = expiresAt
        this.verified = verified
    }
}
