package com.foodapp.user.security

import com.foodapp.common.web.SimpleJwtAuthFilter
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class JwtFilterConfig {
    @Bean
    open fun jwtFilter(@Value("\${security.jwt.secret:change_me_ultra_secret}") secret: String)
        : FilterRegistrationBean<SimpleJwtAuthFilter> {
        val reg = FilterRegistrationBean(SimpleJwtAuthFilter(secret))
        reg.addUrlPatterns("/users/*")
        reg.order = 1
        return reg
    }
}
