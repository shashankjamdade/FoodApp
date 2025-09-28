package com.foodapp.auth.config

import com.foodapp.common.web.SimpleJwtAuthFilter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
open class SecurityConfig(
    @Value("\${app.jwt.secret}") private val secret: String
) {

    @Bean
    open fun jwtAuthFilter(): SimpleJwtAuthFilter {
        return SimpleJwtAuthFilter(secret)
    }

    @Bean
    open fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers("/auth/**").permitAll()
                it.anyRequest().authenticated()
            }
            .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}
