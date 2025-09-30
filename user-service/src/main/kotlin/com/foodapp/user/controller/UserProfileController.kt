package com.foodapp.user.controller

import com.foodapp.common.dto.CreateUserProfileRequest
import com.foodapp.common.dto.UpdateUserProfileRequest
import com.foodapp.common.dto.UserProfileResponse
import com.foodapp.user.service.UserProfileService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1")
class UserProfileController(
    private val userProfileService: UserProfileService
) {
    @GetMapping("/users/{userId}/profiles")
    fun getUserProfile(@PathVariable userId: UUID): UserProfileResponse {
        return userProfileService.getUserProfile(userId)
    }

    @GetMapping("/auth-users/{authUserId}/profiles")
    fun getProfilesByAuthUserId(@PathVariable authUserId: Int): List<UserProfileResponse> {
        return userProfileService.getProfilesByAuthUserId(authUserId)
    }

    @GetMapping("/auth-users/{authUserId}/profiles/default")
    fun getDefaultProfile(@PathVariable authUserId: Int): UserProfileResponse? {
        return userProfileService.getDefaultProfile(authUserId)
    }

    @PostMapping("/profiles")
    @ResponseStatus(HttpStatus.CREATED)
    fun createUserProfile(@RequestBody request: CreateUserProfileRequest): UserProfileResponse {
        return userProfileService.createUserProfile(request)
    }

    @PutMapping("/profiles/{profileId}")
    fun updateUserProfile(
        @PathVariable profileId: UUID,
        @RequestBody request: UpdateUserProfileRequest
    ): UserProfileResponse {
        return userProfileService.updateUserProfile(profileId, request)
    }
}
