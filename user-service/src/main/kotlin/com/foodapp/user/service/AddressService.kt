package com.foodapp.user.service

import com.foodapp.common.dto.AddressDto
import com.foodapp.common.dto.CreateAddressRequest
import com.foodapp.common.dto.UpdateAddressRequest
import com.foodapp.user.domain.model.Address
import com.foodapp.user.repository.AddressRepository
import com.foodapp.user.repository.UserProfileRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
open class AddressService(
    private val addressRepository: AddressRepository,
    private val userProfileRepository: UserProfileRepository
) {

    fun getAddresses(userId: UUID): List<AddressDto> {
        return addressRepository.findByUserProfileUserId(userId).map { it.toDto() }
    }

    fun getDefaultAddress(userId: UUID): AddressDto? {
        return addressRepository.findByUserProfileUserIdAndIsDefaultTrue(userId)?.toDto()
    }

    @Transactional
    fun createAddress(userId: UUID, request: CreateAddressRequest): AddressDto {
        val userProfile = userProfileRepository.findById(userId)
            .orElseThrow { NoSuchElementException("User profile not found") }

        if (request.isDefault) {
            // Remove default flag from existing default address
            addressRepository.findByUserProfileUserIdAndIsDefaultTrue(userId)?.let {
                it.isDefault = false
                addressRepository.save(it)
            }
        }

        val address = Address(
            userProfile = userProfile,
            addressLine1 = request.addressLine1,
            addressLine2 = request.addressLine2,
            city = request.city,
            state = request.state,
            postalCode = request.postalCode,
            country = request.country,
            landmark = request.landmark,
            addressType = request.addressType,
            isDefault = request.isDefault
        )

        return addressRepository.save(address).toDto()
    }

    @Transactional
    fun updateAddress(userId: UUID, addressId: UUID, request: UpdateAddressRequest): AddressDto {
        val address = addressRepository.findById(addressId)
            .orElseThrow { NoSuchElementException("Address not found") }

        if (address.userProfile.userId != userId) {
            throw IllegalStateException("Address does not belong to user")
        }

        request.addressLine1?.let { address.addressLine1 = it }
        request.addressLine2?.let { address.addressLine2 = it }
        request.city?.let { address.city = it }
        request.state?.let { address.state = it }
        request.postalCode?.let { address.postalCode = it }
        request.country?.let { address.country = it }
        request.landmark?.let { address.landmark = it }
        request.addressType?.let { address.addressType = it }
        request.isDefault?.let {
            if (it && !address.isDefault) {
                // Remove default flag from existing default address
                addressRepository.findByUserProfileUserIdAndIsDefaultTrue(userId)?.let { defaultAddress ->
                    defaultAddress.isDefault = false
                    addressRepository.save(defaultAddress)
                }
            }
            address.isDefault = it
        }

        address.updatedAt = LocalDateTime.now()
        return addressRepository.save(address).toDto()
    }

    @Transactional
    fun deleteAddress(userId: UUID, addressId: UUID) {
        val address = addressRepository.findById(addressId)
            .orElseThrow { NoSuchElementException("Address not found") }

        if (address.userProfile.userId != userId) {
            throw IllegalStateException("Address does not belong to user")
        }

        addressRepository.delete(address)
    }

    private fun Address.toDto() = AddressDto(
        id = id,
        addressLine1 = addressLine1,
        addressLine2 = addressLine2,
        city = city,
        state = state,
        postalCode = postalCode,
        country = country,
        landmark = landmark,
        addressType = addressType,
        isDefault = isDefault
    )
}
