package com.foodapp.auth.controller

import com.foodapp.auth.dto.*
import com.foodapp.auth.repository.UserRepository
import com.foodapp.auth.service.AuthService
import com.foodapp.common.security.JwtUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {


    private val enc = BCryptPasswordEncoder()
    @Value("\${app.jwt.secret}")
    private var secret: String = ""
    @Autowired
    lateinit var userRepository: UserRepository;


    @PostMapping("/signup")
    fun signup(@RequestBody request: SignupRequest): ResponseEntity<SignupResponse> {
        val result = authService.signup(request)
        return ResponseEntity.ok(result)
    }

    @PostMapping("/verify")
    fun verifyEmail(@RequestBody request: VerifyOtpRequest): ResponseEntity<VerifyOtpResponse> {
        val result = authService.verifyEmail(request)
        return ResponseEntity.ok(result)
    }

    @PostMapping("/resend-otp")
    fun resendOtp(@RequestParam email: String): ResponseEntity<SignupResponse> {
        val result = authService.resendOtp(email)
        return ResponseEntity.ok(result)
    }


    @PostMapping("/login")
    fun login(@RequestBody req: Map<String, String>): ResponseEntity<Any> {
        val email = req["email"] ?: return ResponseEntity.badRequest().build()
        val user = userRepository.findByEmail(email) ?: return ResponseEntity.status(401).body(mapOf("error" to "invalid credentials"))
        if (!enc.matches(req["password"], user.passwordHash)) return ResponseEntity.status(401).body(mapOf("error" to "invalid credentials"))

        val token = JwtUtils.create(secret, user.id.toString(), user.role.name, 30 * 60 * 1000)
        return ResponseEntity.ok(mapOf("accessToken" to token, "role" to user.role))
    }
}
