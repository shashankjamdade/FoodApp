package com.foodapp.user.domain.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "addresses")
open class Address {
    @Id
    @get:Column(name = "id")
    open var id: UUID = UUID.randomUUID()

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    open lateinit var userProfile: UserProfile

    @get:Column(nullable = false)
    open var addressLine1: String = ""

    @get:Column(nullable = true)
    open var addressLine2: String? = null

    @get:Column(nullable = false)
    open var city: String = ""

    @get:Column(nullable = false)
    open var state: String = ""

    @get:Column(nullable = false)
    open var postalCode: String = ""

    @get:Column(nullable = false)
    open var country: String = "India"

    @get:Column(nullable = true)
    open var landmark: String? = null

    @get:Column(nullable = false)
    open var addressType: String = ""

    @get:Column(nullable = false)
    open var isDefault: Boolean = false

    @get:Column(name = "created_at")
    open var createdAt: LocalDateTime = LocalDateTime.now()

    @get:Column(name = "updated_at")
    open var updatedAt: LocalDateTime = LocalDateTime.now()

    constructor()

    constructor(
        userProfile: UserProfile,
        addressLine1: String,
        city: String,
        state: String,
        postalCode: String,
        addressType: String,
        addressLine2: String? = null,
        country: String = "India",
        landmark: String? = null,
        isDefault: Boolean = false
    ) {
        this.userProfile = userProfile
        this.addressLine1 = addressLine1
        this.addressLine2 = addressLine2
        this.city = city
        this.state = state
        this.postalCode = postalCode
        this.country = country
        this.landmark = landmark
        this.addressType = addressType
        this.isDefault = isDefault
    }
}
