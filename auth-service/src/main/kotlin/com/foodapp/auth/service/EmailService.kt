package com.foodapp.auth.service

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Value

@Service
class EmailService(
    private val mailSender: JavaMailSender
) {
    @Value("\${spring.mail.username}")
    private lateinit var fromEmail: String

    @Value("\${app.email.otp.expiry-minutes}")
    private lateinit var otpExpiryMinutes: String

    fun sendOtpEmail(toEmail: String, otp: String) {
        println("FROMMAIL ${fromEmail}")
        val message = SimpleMailMessage()
        message.setFrom(fromEmail)
        message.setTo(toEmail)
        message.setSubject("Your FoodApp Email Verification Code")
        message.setText("""
            Hello,
            
            Your email verification code is: $otp
            
            This code will expire in $otpExpiryMinutes minutes.
            
            If you didn't request this code, please ignore this email.
            
            Best regards,
            FoodApp Team
        """.trimIndent())

        mailSender.send(message)
    }
}
