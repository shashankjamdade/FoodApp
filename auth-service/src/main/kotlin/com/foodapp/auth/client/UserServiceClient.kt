package com.foodapp.auth.client

import com.foodapp.common.dto.CreateUserProfileRequest
import com.foodapp.common.dto.UserProfileResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "user-service", url = "\${app.services.user.url}")
interface UserServiceClient {
    @PostMapping("/api/v1/profiles")
    fun createProfile(@RequestBody request: CreateUserProfileRequest): UserProfileResponse
}
