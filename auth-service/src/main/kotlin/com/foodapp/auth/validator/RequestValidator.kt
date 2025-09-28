package com.foodapp.auth.validator

import com.foodapp.auth.dto.SignupRequest
import com.foodapp.auth.entity.Role
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

object RequestValidator {
    fun validateSignupRequest(request: SignupRequest) {
        // Validate email format
        if (!isValidEmail(request.email)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email format")
        }

        // Validate password length
        if (request.password.length < 6) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must be at least 6 characters long")
        }

        // Validate role
        if (!Role.values().contains(request.role)) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Invalid role. Must be one of: ${Role.values().joinToString(", ")}"
            )
        }

        // Validate name fields
        if (request.firstName.isBlank() || request.lastName.isBlank()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "First name and last name cannot be empty")
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return email.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)\$"))
    }
}
