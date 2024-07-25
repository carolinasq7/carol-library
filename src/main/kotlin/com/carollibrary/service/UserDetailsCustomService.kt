package com.carollibrary.service

import com.carollibrary.exception.AuthenticationException
import com.carollibrary.repository.CustomerRepository
import com.carollibrary.security.UserCustomDetails
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsCustomService(
    private val customerRepository: CustomerRepository
): UserDetailsService {
    override fun loadUserByUsername(id: String): UserDetails {
        val customer = customerRepository.findById(id.toInt())
            .orElseThrow{ AuthenticationException("User not Found", "99")}
        return UserCustomDetails(customer)
    }
}