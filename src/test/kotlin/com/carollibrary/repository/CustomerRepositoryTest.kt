package com.carollibrary.repository

import com.carollibrary.helper.buildCustomer
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class CustomerRepositoryTest {

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @BeforeEach
    fun setup() = customerRepository.deleteAll()

    @Test
    fun `Should return name containing`() {
       val fiona = customerRepository.save(buildCustomer(name = "Fiona"))
       val fiori = customerRepository.save(buildCustomer(name = "Fiori"))
       val pageable: PageRequest = PageRequest.of(0, 10)

       val customerFind = customerRepository.findByNameContaining("fio", pageable)

        assertEquals(listOf(fiona, fiori), customerFind.content)
    }

    @Nested
    inner class `Exists by email` {
        @Test
        fun `Should return true when email exists`() {
            val email = "email@teste.com"
                customerRepository.save(buildCustomer(email = email))

            val exists = customerRepository.existsByEmail(email)

            assertTrue(exists)
        }

        @Test
        fun `Should return false when email exists`() {
            val email = "nonexistengemail@teste.com"
            val exists = customerRepository.existsByEmail(email)

            assertFalse(exists)
        }
    }

    @Nested
    inner class `Find by email` {
        @Test
        fun `Should return customer when email find`() {
            val email = "email@teste.com"
            val customer = customerRepository.save(buildCustomer(email = email))

            val result = customerRepository.findByEmail(email)

            assertNotNull(result)
            assertEquals(customer, result)
        }

        @Test
        fun `Should return null when email do not exists`() {
            val email = "nonexistengemail@teste.com"
            val result = customerRepository.findByEmail(email)

            assertNull(result)
        }
    }
}