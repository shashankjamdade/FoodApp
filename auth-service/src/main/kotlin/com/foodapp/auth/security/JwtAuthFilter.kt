package com.foodapp.auth.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys

open class JwtAuthFilter(
    private val jwtSecret: String
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val authHeader = request.getHeader("Authorization")
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                val token = authHeader.substring(7)
                val claims = validateAndParseToken(token)

                val authorities = (claims["authorities"] as? List<*>)?.mapNotNull {
                    if (it is String) SimpleGrantedAuthority(it) else null
                } ?: emptyList()

                val authentication = UsernamePasswordAuthenticationToken(
                    claims.subject,
                    null,
                    authorities
                )
                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (e: Exception) {
            // If token validation fails, just continue without setting authentication
            logger.debug("Token validation failed", e)
        }

        filterChain.doFilter(request, response)
    }

    private fun validateAndParseToken(token: String): Claims {
        val keyBytes = Decoders.BASE64.decode(jwtSecret)
        val key = Keys.hmacShaKeyFor(keyBytes)

        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload
    }
}
