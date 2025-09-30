package com.foodapp.user.domain.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "favorites")
open class Favorite {
    @Id
    @get:Column(name = "id")
    open var id: UUID = UUID.randomUUID()

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    open lateinit var userProfile: UserProfile

    @get:Column(name = "restaurant_id", nullable = false)
    open var restaurantId: UUID = UUID.randomUUID()

    @get:Column(name = "created_at")
    open var createdAt: LocalDateTime = LocalDateTime.now()

    constructor()

    constructor(
        userProfile: UserProfile,
        restaurantId: UUID = UUID.randomUUID(),
        createdAt: LocalDateTime = LocalDateTime.now()
    ) {
        this.userProfile = userProfile
        this.restaurantId = restaurantId
        this.createdAt = createdAt
    }
}
