package com.foodapp.user.controller

import com.foodapp.common.dto.AddressDto
import com.foodapp.common.dto.CreateAddressRequest
import com.foodapp.common.dto.UpdateAddressRequest
import com.foodapp.user.service.AddressService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/users/{userId}/addresses")
class AddressController(
    private val addressService: AddressService
) {

    @GetMapping
    fun getAddresses(@PathVariable userId: UUID): List<AddressDto> {
        return addressService.getAddresses(userId)
    }

    @GetMapping("/default")
    fun getDefaultAddress(@PathVariable userId: UUID): AddressDto? {
        return addressService.getDefaultAddress(userId)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createAddress(
        @PathVariable userId: UUID,
        @RequestBody request: CreateAddressRequest
    ): AddressDto {
        return addressService.createAddress(userId, request)
    }

    @PutMapping("/{addressId}")
    fun updateAddress(
        @PathVariable userId: UUID,
        @PathVariable addressId: UUID,
        @RequestBody request: UpdateAddressRequest
    ): AddressDto {
        return addressService.updateAddress(userId, addressId, request)
    }

    @DeleteMapping("/{addressId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteAddress(
        @PathVariable userId: UUID,
        @PathVariable addressId: UUID
    ) {
        addressService.deleteAddress(userId, addressId)
    }
}
