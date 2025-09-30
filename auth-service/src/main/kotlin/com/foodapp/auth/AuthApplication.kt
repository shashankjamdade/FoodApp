package com.foodapp.auth

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
open class AuthApplication

fun main(args: Array<String>) {
    runApplication<AuthApplication>(*args)
}
