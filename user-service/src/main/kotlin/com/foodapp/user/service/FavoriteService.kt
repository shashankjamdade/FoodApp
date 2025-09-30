package com.foodapp.user.service

import com.foodapp.common.dto.FavoriteDto
import com.foodapp.common.dto.AddFavoriteRequest
import com.foodapp.user.domain.model.Favorite
import com.foodapp.user.repository.FavoriteRepository
import com.foodapp.user.repository.UserProfileRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
open class FavoriteService(
    private val favoriteRepository: FavoriteRepository,
    private val userProfileRepository: UserProfileRepository
) {

    fun getFavorites(userId: UUID): List<FavoriteDto> {
        return favoriteRepository.findByUserProfileUserId(userId).map { it.toDto() }
    }

    @Transactional
    fun addFavorite(userId: UUID, request: AddFavoriteRequest): FavoriteDto {
        val userProfile = userProfileRepository.findById(userId)
            .orElseThrow { NoSuchElementException("User profile not found") }

        // Check if restaurant is already in favorites
        favoriteRepository.findByUserProfileUserIdAndRestaurantId(userId, request.restaurantId)?.let {
            throw IllegalStateException("Restaurant is already in favorites")
        }

        val favorite = Favorite(
            userProfile = userProfile,
            restaurantId = request.restaurantId
        )

        return favoriteRepository.save(favorite).toDto()
    }

    @Transactional
    fun removeFavorite(userId: UUID, restaurantId: UUID) {
        val favorite = favoriteRepository.findByUserProfileUserIdAndRestaurantId(userId, restaurantId)
            ?: throw NoSuchElementException("Favorite not found")

        favoriteRepository.delete(favorite)
    }

    private fun Favorite.toDto() = FavoriteDto(
        id = id,
        restaurantId = restaurantId
    )
}
