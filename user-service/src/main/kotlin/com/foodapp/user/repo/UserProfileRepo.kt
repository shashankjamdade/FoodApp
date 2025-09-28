package com.foodapp.user.repo

import com.foodapp.user.entity.UserProfile
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserProfileRepo : JpaRepository<UserProfile, UUID>
