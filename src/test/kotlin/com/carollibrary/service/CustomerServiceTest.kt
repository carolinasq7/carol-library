package com.carollibrary.service

import com.carollibrary.enums.CustomerStatus
import com.carollibrary.enums.Errors
import com.carollibrary.helper.buildCustomer
import com.carollibrary.exception.NotFoundException
import com.carollibrary.repository.CustomerRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.Optional
import java.util.UUID
import kotlin.random.Random

@ExtendWith(MockKExtension::class)
class CustomerServiceTest {
    @MockK
    private lateinit var customerRepository: CustomerRepository

    @MockK
    private lateinit var bookService: BookService

    @MockK
    private lateinit var bCrypt: BCryptPasswordEncoder

    @InjectMockKs
    @SpyK
    private lateinit var customerService: CustomerService

    @Test
    fun `Should return all customers`() {
        val fakeCustomers = listOf(buildCustomer(), buildCustomer())
        val pageable = PageRequest.of(0, 10)
        val fakePage = PageImpl(fakeCustomers, pageable, fakeCustomers.size.toLong())

        every { customerRepository.findAll(pageable) } returns fakePage

        val customers = customerService.getAllCustomers(null, pageable)

        assertEquals(fakePage, customers)
        verify(exactly = 1) { customerRepository.findAll(pageable) }
        verify(exactly = 0) { customerRepository.findByNameContaining("test", pageable) }
    }

    @Test
    fun `Should return customers when name is informed`() {
        val name = Math.random().toString()
        val fakeCustomers = listOf(buildCustomer(), buildCustomer())
        val pageable = PageRequest.of(0, 10)
        val fakePageCustomer = PageImpl(fakeCustomers, pageable, fakeCustomers.size.toLong())

        every { customerRepository.findByNameContaining(name, pageable) } returns fakePageCustomer

        val customers = customerService.getAllCustomers(name, pageable)

        assertEquals(fakePageCustomer, customers)
        verify(exactly = 1) { customerRepository.findByNameContaining(name, pageable) }
        verify(exactly = 0) { customerRepository.findAll(pageable) }
    }

    @Test
    fun `Should create customer and encrypt password`() {
        val initialPassword = Math.random().toString()
        val fakeCustomers = buildCustomer(password = initialPassword)
        val fakePasswordEncrypt = UUID.randomUUID().toString()
        val fakeCustomerPassword = fakeCustomers.copy(password = fakePasswordEncrypt)

        every { customerRepository.save(fakeCustomerPassword) } returns fakeCustomers
        every { bCrypt.encode(initialPassword) } returns fakePasswordEncrypt

        customerService.create(fakeCustomers)

        verify(exactly = 1) { customerRepository.save(fakeCustomerPassword) }
        verify(exactly = 1) { bCrypt.encode(initialPassword) }
    }

    @Test
    fun `Should return customer by id`() {
        val id = Random.nextInt()
        val fakeCustomer = buildCustomer(id = id)

        every { customerRepository.findById(id) } returns Optional.of(fakeCustomer)

        val customer = customerService.findById(id)

        assertEquals(fakeCustomer, customer)
        verify(exactly = 1) { customerRepository.findById(id) }
    }

    @Test
    fun `Should throw not found exception when find by id`() {
        val id = Random.nextInt()

        every { customerRepository.findById(id) } returns Optional.empty()

        val error = assertThrows<NotFoundException>() {
            customerService.findById(id)
        }

        assertEquals("Customer [${id}] not exists", error.message)
        assertEquals("ML-201", error.errorCode)
        verify(exactly = 1) { customerRepository.findById(id) }
    }

    @Test
    fun `Should update customer`() {
        val id = Random.nextInt()
        val fakeCustomer = buildCustomer(id = id)

        every { customerRepository.existsById(id) } returns true
        every { customerRepository.save(fakeCustomer) } returns fakeCustomer

        customerService.update(fakeCustomer)

        verify(exactly = 1) { customerRepository.existsById(id) }
        verify(exactly = 1) { customerRepository.save(fakeCustomer) }
    }

    @Test
    fun `Should throw not found exception when update customer`() {
        val id = Random.nextInt()
        val fakeCustomer = buildCustomer(id = id)

        every { customerRepository.existsById(id) } returns false
        every { customerRepository.save(fakeCustomer) } returns fakeCustomer

        val error = assertThrows<NotFoundException>() {
            customerService.update(fakeCustomer)
        }

        assertEquals("Customer [${id}] not exists", error.message)
        assertEquals("ML-201", error.errorCode)

        verify(exactly = 1) { customerRepository.existsById(id) }
        verify(exactly = 0) { customerRepository.save(any()) }
    }

    @Test
    fun `Should delete customer`() {
        val id = Random.nextInt()
        val fakeCustomer = buildCustomer(id = id)
        val expectedCustomer = fakeCustomer.copy(status = CustomerStatus.INACTIVE)

        every { customerService.findById(id) } returns fakeCustomer
        every { customerRepository.save(expectedCustomer) } returns expectedCustomer
        every { bookService.deleteByCustomer(fakeCustomer) } just runs

        customerService.delete(id)

        verify(exactly = 1) { customerService.findById(id) }
        verify(exactly = 1) { bookService.deleteByCustomer(fakeCustomer) }
        verify(exactly = 1) { customerRepository.save(expectedCustomer) }
    }

    @Test
    fun `Should throw not found exception when delete customer`() {
        val id = Random.nextInt()

        every { customerService.findById(id) } throws NotFoundException(
            Errors.ML201.message.format(id),
            Errors.ML201.code
        )

        val error = assertThrows<NotFoundException>() {
            customerService.delete(id)
        }

        assertEquals("Customer [${id}] not exists", error.message)
        assertEquals("ML-201", error.errorCode)

        verify(exactly = 1) { customerService.findById(id) }
        verify(exactly = 0) { bookService.deleteByCustomer(any()) }
        verify(exactly = 0) { customerRepository.save(any()) }
    }

    @Test
    fun `Should return true when email available`() {
        val email = "${UUID.randomUUID().toString()}@email.com"

        every { customerRepository.existsByEmail(email) } returns false

        val emailAvailabe = customerService.emailAvailable(email)

        assertTrue(emailAvailabe)
        verify(exactly = 1) { customerRepository.existsByEmail(email) }
    }

    @Test
    fun `Should return false when email available`() {
        val email = "${UUID.randomUUID()}@email.com"

        every { customerRepository.existsByEmail(email) } returns true

        val emailAvailabe = customerService.emailAvailable(email)

        assertFalse(emailAvailabe)
        verify(exactly = 1) { customerRepository.existsByEmail(email) }
    }
}