package com.foodapp.user.repository

import com.foodapp.user.domain.model.Favorite
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface FavoriteRepository : JpaRepository<Favorite, UUID> {
    fun findByUserProfileUserId(userId: UUID): List<Favorite>
    fun findByUserProfileUserIdAndRestaurantId(userId: UUID, restaurantId: UUID): Favorite?
    fun deleteByUserProfileUserIdAndRestaurantId(userId: UUID, restaurantId: UUID)
}
