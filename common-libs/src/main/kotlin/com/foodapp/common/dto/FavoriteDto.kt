package com.foodapp.common.dto

import java.util.*

data class FavoriteDto(
    val id: UUID,
    val restaurantId: UUID
)

data class AddFavoriteRequest(
    val restaurantId: UUID
)
