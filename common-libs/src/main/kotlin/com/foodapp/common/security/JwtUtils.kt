package com.foodapp.common.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import java.util.*

object JwtUtils {

    fun create(secret: String, subject: String, role: String, expMillis: Long): String {
        val key = Keys.hmacShaKeyFor(secret.toByteArray())
        val now = Date()

        return Jwts.builder()
            .subject(subject)
            .claim("role", role)
            .issuedAt(now)
            .expiration(Date(now.time + expMillis))
            .signWith(key)
            .compact()
    }

    fun parseClaims(secret: String, token: String): Map<String, Any> {
        val key = Keys.hmacShaKeyFor(secret.toByteArray())
        val claims = Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload

        return claims
    }
}

