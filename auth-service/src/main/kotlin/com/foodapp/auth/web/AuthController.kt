//package com.foodapp.auth.web
//
//import com.foodapp.auth.entity.User
//import com.foodapp.auth.repo.UserRepo
//import com.foodapp.common.security.JwtUtils
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.http.ResponseEntity
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
//import org.springframework.web.bind.annotation.*
//
//@RestController
//@RequestMapping("/auth")
//class AuthController(private val repo: UserRepo) {
//
//    private val enc = BCryptPasswordEncoder()
//    @Value("\${app.jwt.secret}")
//    private var secret: String = ""
//
//    @PostMapping("/signup")
//    fun signup(@RequestBody req: Map<String, String>): ResponseEntity<Any> {
//        val email = req["email"] ?: return ResponseEntity.badRequest().body(mapOf("error" to "email required"))
//        val password = req["password"] ?: return ResponseEntity.badRequest().body(mapOf("error" to "password required"))
//        val role = req["role"] ?: "CUSTOMER"
//        val saved = repo.save(User(
//            email = email, passwordHash = enc.encode(password), role = role,
//                firstName = "Shashank",
//            lastName = "Jamdade"
//        ))
//        return ResponseEntity.ok(mapOf("id" to saved.id.toString(), "email" to saved.email, "role" to saved.role))
//    }
//
//    @PostMapping("/login")
//    fun login(@RequestBody req: Map<String, String>): ResponseEntity<Any> {
//        val email = req["email"] ?: return ResponseEntity.badRequest().build()
//        val user = repo.findByEmail(email).orElse(null) ?: return ResponseEntity.status(401).body(mapOf("error" to "invalid credentials"))
//        if (!enc.matches(req["password"], user.passwordHash)) return ResponseEntity.status(401).body(mapOf("error" to "invalid credentials"))
//
//        val token = JwtUtils.create(secret, user.id.toString(), user.role, 30 * 60 * 1000)
//        return ResponseEntity.ok(mapOf("accessToken" to token, "role" to user.role))
//    }
//}
