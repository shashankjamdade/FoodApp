package com.foodapp.gateway.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
open class JwtAuthFilter(
    @Value("\${security.jwt.secret:change_me_ultra_secret}") private val secret: String
) : GlobalFilter {

    override fun filter(exchange: ServerWebExchange, chain: org.springframework.cloud.gateway.filter.GatewayFilterChain): Mono<Void> {
        val auth = exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION)
        if (auth != null && auth.startsWith("Bearer ")) {
            try {
                val claims = Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.toByteArray())).build()
                    .parseSignedClaims(auth.substring(7)).payload
                val role = claims["role"]?.toString()
                val user = claims.subject
                val mutated = exchange.mutate().request { r ->
                    r.headers { h ->
                        if (role != null) h["X-Role"] = listOf(role)
                        if (user != null) h["X-User-Id"] = listOf(user)
                    }
                }.build()
                return chain.filter(mutated)
            } catch (_: Exception) { /* ignore */ }
        }
        return chain.filter(exchange)
    }
}
