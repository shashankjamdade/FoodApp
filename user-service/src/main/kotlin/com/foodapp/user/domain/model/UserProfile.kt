package com.foodapp.user.domain.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "user_profiles")
open class UserProfile {
    @Id
    @get:Column(name = "user_id")
    open var userId: UUID = UUID.randomUUID()

    @get:Column(name = "auth_user_id", nullable = false)
    open var authUserId: Int = 0

    @get:Column(nullable = false)
    open var isDefault: Boolean = false

    @get:Column(nullable = false)
    open var firstName: String = ""

    @get:Column(nullable = true)
    open var lastName: String? = null

    @get:Column(nullable = false, unique = true)
    open var email: String = ""

    @get:Column(nullable = false, unique = true)
    open var phone: String = ""

    @get:Enumerated(EnumType.STRING)
    @get:Column(name = "user_role", nullable = false)
    open var role: UserRole = UserRole.CUSTOMER

    @get:Column(nullable = true)
    open var profilePicture: String? = null

    @OneToMany(mappedBy = "userProfile", cascade = [CascadeType.ALL], orphanRemoval = true)
    open var addresses: MutableList<Address> = mutableListOf()

    @OneToMany(mappedBy = "userProfile", cascade = [CascadeType.ALL], orphanRemoval = true)
    open var favorites: MutableList<Favorite> = mutableListOf()

    @OneToOne(mappedBy = "userProfile", cascade = [CascadeType.ALL])
    open var wallet: Wallet? = null

    @get:Column(name = "created_at")
    open var createdAt: LocalDateTime = LocalDateTime.now()

    @get:Column(name = "updated_at")
    open var updatedAt: LocalDateTime = LocalDateTime.now()

    constructor()

    constructor(
        authUserId: Int,
        firstName: String,
        email: String,
        phone: String,
        lastName: String? = null,
        role: UserRole = UserRole.CUSTOMER,
        profilePicture: String? = null,
        isDefault: Boolean = false
    ) {
        this.authUserId = authUserId
        this.firstName = firstName
        this.lastName = lastName
        this.email = email
        this.phone = phone
        this.role = role
        this.profilePicture = profilePicture
        this.isDefault = isDefault
    }
}
