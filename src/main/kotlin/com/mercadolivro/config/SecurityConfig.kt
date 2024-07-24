package com.mercadolivro.config

import com.mercadolivro.enums.Role
import com.mercadolivro.repository.CustomerRepository
import com.mercadolivro.security.AuthenticationFilter
import com.mercadolivro.security.AuthorizationFilter
import com.mercadolivro.security.CustomAuthenticationEntryPoint
import com.mercadolivro.security.JwtUtil
import com.mercadolivro.service.UserDetailsCustomService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    private val customerRepository: CustomerRepository,
    private val userDetails: UserDetailsCustomService,
    private val jwtUtil: JwtUtil,
    private val customEntryPoint: CustomAuthenticationEntryPoint
) {

    private val PUBLIC_POST_MATCHERS = arrayOf("/customers")
    private val ADMIN_MATCHERS = arrayOf("/admin/**")

    fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetails).passwordEncoder(BCryptPasswordEncoder())
    }

    @Bean
    fun filterChain(http: HttpSecurity, authenticationManager: AuthenticationManager): SecurityFilterChain {
        return http
            .cors {}
            .csrf { it.disable() }
            .sessionManagement { session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { auth ->
                auth.requestMatchers(HttpMethod.POST, *PUBLIC_POST_MATCHERS).permitAll()
                auth.requestMatchers(*ADMIN_MATCHERS).hasAuthority(Role.ADMIN.description)
                    .anyRequest().authenticated()
            }
            .addFilter(AuthenticationFilter(authenticationManager, customerRepository, jwtUtil))
            .addFilter(AuthorizationFilter(authenticationManager, userDetails, jwtUtil))
            .exceptionHandling { handling -> handling.authenticationEntryPoint(customEntryPoint) }
            .build()
    }

    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authenticationManager(http: HttpSecurity): AuthenticationManager {
        val authManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder::class.java)
        return authManagerBuilder.build()
    }

}
