package com.foodapp.user.repository

import com.foodapp.user.domain.model.UserProfile
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserProfileRepository : JpaRepository<UserProfile, UUID> {
    fun findByEmail(email: String): UserProfile?
    fun findByPhone(phone: String): UserProfile?
    fun existsByEmail(email: String): Boolean
    fun existsByPhone(phone: String): Boolean
    fun findByAuthUserId(authUserId: Int): List<UserProfile>
    fun findByAuthUserIdAndIsDefaultTrue(authUserId: Int): UserProfile?
}
