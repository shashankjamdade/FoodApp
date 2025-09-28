package com.foodapp.user.web

import com.foodapp.user.entity.UserProfile
import com.foodapp.user.repo.UserProfileRepo
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/users")
class UserController(private val repo: UserProfileRepo) {

    @GetMapping("/me")
    fun me(@RequestHeader("X-User-Id", required = false) uid: String?): ResponseEntity<Any> {
        if (uid == null) return ResponseEntity.status(401).body(mapOf("error" to "missing token"))
        val id = UUID.fromString(uid)
        val profile = repo.findById(id).orElse(null)
        return if (profile == null) ResponseEntity.ok(mapOf("userId" to uid, "hasProfile" to false)) else ResponseEntity.ok(profile)
    }

    @PostMapping("/profile")
    fun upsert(@RequestHeader("X-User-Id", required = false) uid: String?, @RequestBody payload: UserProfile): ResponseEntity<Any> {
        if (uid == null) return ResponseEntity.status(401).body(mapOf("error" to "missing token"))
        payload.userId = UUID.fromString(uid)
        return ResponseEntity.ok(repo.save(payload))
    }
}
