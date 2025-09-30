package com.foodapp.user.service

import com.foodapp.common.dto.*
import com.foodapp.user.domain.model.UserProfile
import com.foodapp.user.domain.model.UserRole
import com.foodapp.user.repository.UserProfileRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
open class UserProfileService(
    private val userProfileRepository: UserProfileRepository,
    private val walletService: WalletService,
    private val emailVerificationService: EmailVerificationService
) {

    fun getUserProfile(userId: UUID): UserProfileResponse {
        val profile = userProfileRepository.findById(userId)
            .orElseThrow { NoSuchElementException("User profile not found") }
        return profile.toDto()
    }

    fun getProfilesByAuthUserId(authUserId: Int): List<UserProfileResponse> {
        return userProfileRepository.findByAuthUserId(authUserId).map { it.toDto() }
    }

    fun getDefaultProfile(authUserId: Int): UserProfileResponse? {
        return userProfileRepository.findByAuthUserIdAndIsDefaultTrue(authUserId)?.toDto()
    }

    @Transactional
    fun createUserProfile(request: CreateUserProfileRequest): UserProfileResponse {
        if (!emailVerificationService.isEmailVerified(request.email)) {
            throw IllegalStateException("Email must be verified before creating profile")
        }

        if (userProfileRepository.existsByEmail(request.email)) {
            throw IllegalStateException("Email already exists")
        }
        if (userProfileRepository.existsByPhone(request.phone)) {
            throw IllegalStateException("Phone number already exists")
        }

        // Validate role
        val role = try {
            UserRole.valueOf(request.role.uppercase())
        } catch (_: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid user role. Allowed values are: ${UserRole.entries.joinToString()}")
        }

        // If this is the first profile for the user, make it default
        val isDefault = !userProfileRepository.findByAuthUserId(request.authUserId).any()

        val profile = UserProfile(
            authUserId = request.authUserId,
            firstName = request.firstName,
            lastName = request.lastName,
            email = request.email,
            phone = request.phone,
            role = role,
            isDefault = isDefault
        )

        val savedProfile = userProfileRepository.save(profile)
        walletService.createWallet(savedProfile)

        return savedProfile.toDto()
    }

    @Transactional
    fun updateUserProfile(userId: UUID, request: UpdateUserProfileRequest): UserProfileResponse {
        val profile = userProfileRepository.findById(userId)
            .orElseThrow { NoSuchElementException("User profile not found") }

        request.firstName?.let { profile.firstName = it }
        request.lastName?.let { profile.lastName = it }
        request.phone?.let {
            if (userProfileRepository.existsByPhone(it) && profile.phone != it) {
                throw IllegalStateException("Phone number already exists")
            }
            profile.phone = it
        }
        request.role?.let {
            val newRole = try {
                UserRole.valueOf(it.uppercase())
            } catch (_: IllegalArgumentException) {
                throw IllegalArgumentException("Invalid user role. Allowed values are: ${UserRole.entries.joinToString()}")
            }
            profile.role = newRole
        }
        request.profilePicture?.let { profile.profilePicture = it }

        // Handle default profile change
        request.isDefault?.let {
            if (it && !profile.isDefault) {
                // Remove default flag from existing default profile
                userProfileRepository.findByAuthUserIdAndIsDefaultTrue(profile.authUserId)?.let { defaultProfile ->
                    defaultProfile.isDefault = false
                    userProfileRepository.save(defaultProfile)
                }
            }
            profile.isDefault = it
        }

        profile.updatedAt = LocalDateTime.now()
        return userProfileRepository.save(profile).toDto()
    }

    private fun UserProfile.toDto() = UserProfileResponse(
        userId = userId,
        authUserId = authUserId,
        firstName = firstName,
        lastName = lastName,
        email = email,
        phone = phone,
        role = role.name,
        profilePicture = profilePicture,
        isDefault = isDefault,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
