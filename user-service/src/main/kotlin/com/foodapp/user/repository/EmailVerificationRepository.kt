package com.foodapp.user.repository

import com.foodapp.user.domain.model.EmailVerification
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface EmailVerificationRepository : JpaRepository<EmailVerification, UUID> {
    fun findByEmailAndVerifiedFalseOrderByCreatedAtDesc(email: String): EmailVerification?
    fun existsByEmailAndVerifiedTrue(email: String): Boolean
}
