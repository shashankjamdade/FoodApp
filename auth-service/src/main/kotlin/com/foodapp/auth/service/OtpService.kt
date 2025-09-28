package com.foodapp.auth.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import kotlin.random.Random

@Service
class OtpService {
    @Value("\${app.email.otp.expiry-minutes}")
    private var otpExpiryMinutes: Long = 10

    fun generateOtp(): String {
        return Random.nextInt(100000, 999999).toString()
    }

    fun getOtpExpiryTime(): LocalDateTime {
        return LocalDateTime.now().plusMinutes(otpExpiryMinutes)
    }

    fun isOtpValid(storedOtp: String?, receivedOtp: String, expiryTime: LocalDateTime?): Boolean {
        if (storedOtp == null || expiryTime == null) {
            return false
        }

        return storedOtp == receivedOtp && LocalDateTime.now().isBefore(expiryTime)
    }
}
