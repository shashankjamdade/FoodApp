package com.foodapp.user.controller

import com.foodapp.common.dto.FavoriteDto
import com.foodapp.common.dto.AddFavoriteRequest
import com.foodapp.user.service.FavoriteService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/users/{userId}/favorites")
class FavoriteController(
    private val favoriteService: FavoriteService
) {

    @GetMapping
    fun getFavorites(@PathVariable userId: UUID): List<FavoriteDto> {
        return favoriteService.getFavorites(userId)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addFavorite(
        @PathVariable userId: UUID,
        @RequestBody request: AddFavoriteRequest
    ): FavoriteDto {
        return favoriteService.addFavorite(userId, request)
    }

    @DeleteMapping("/{restaurantId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun removeFavorite(
        @PathVariable userId: UUID,
        @PathVariable restaurantId: UUID
    ) {
        favoriteService.removeFavorite(userId, restaurantId)
    }
}
