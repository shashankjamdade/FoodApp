package com.foodapp.user.repository

import com.foodapp.user.domain.model.Address
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface AddressRepository : JpaRepository<Address, UUID> {
    fun findByUserProfileUserId(userId: UUID): List<Address>
    fun findByUserProfileUserIdAndIsDefaultTrue(userId: UUID): Address?
}
