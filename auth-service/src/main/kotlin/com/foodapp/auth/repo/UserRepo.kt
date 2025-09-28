package com.foodapp.auth.repo

import com.foodapp.auth.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepo : JpaRepository<User, UUID> {
    fun findByEmail(email: String): Optional<User>
}
