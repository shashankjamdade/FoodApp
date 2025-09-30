package com.foodapp.auth.config

import com.foodapp.auth.security.JwtAuthFilter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
open class SecurityConfig(
    @Value("\${app.jwt.secret}")
    private val jwtSecret: String
) {
    @Bean
    open fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { auth ->
                auth.requestMatchers("/auth/signup", "/auth/login").permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterBefore(JwtAuthFilter(jwtSecret), UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }
}
