package com.foodapp.user.service

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
open class EmailService(
    private val mailSender: JavaMailSender
) {

    fun sendOtpEmail(to: String, otp: String) {
        val message = SimpleMailMessage().apply {
            setTo(to)
            setSubject("FoodApp Email Verification")
            setText("""
                Hello,
                
                Your verification code is: $otp
                
                This code will expire in 5 minutes.
                
                Best regards,
                FoodApp Team
            """.trimIndent())
        }

        mailSender.send(message)
    }
}
