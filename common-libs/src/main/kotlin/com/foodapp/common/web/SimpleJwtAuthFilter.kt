package com.foodapp.common.web

import com.foodapp.common.security.JwtUtils
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.web.filter.OncePerRequestFilter

open class SimpleJwtAuthFilter(private val secret: String) : OncePerRequestFilter() {

    // Public endpoints that should bypass JWT auth
    private val publicEndpoints = setOf(
        "/auth/verify",
        "/auth/resend-otp",
        "/auth/signup",
        "/auth/login"
    )

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val path = request.servletPath

        // 🚨 Skip JWT validation for public endpoints
        if (publicEndpoints.any { path.startsWith(it) }) {
            filterChain.doFilter(request, response)
            return
        }

        val auth = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (auth != null && auth.startsWith("Bearer ")) {
            try {
                val claims = JwtUtils.parseClaims(secret, auth.substring(7))
                request.setAttribute("X-User-Id", claims["sub"]?.toString())
                request.setAttribute("X-Role", claims["role"]?.toString())
            } catch (_: Exception) {
                // invalid token → reject
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token")
                return
            }
        } else {
            // no token → reject
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing Authorization header")
            return
        }

        filterChain.doFilter(request, response)
    }



}
