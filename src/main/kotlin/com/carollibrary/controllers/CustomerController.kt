package com.carollibrary.controllers

import com.carollibrary.controllers.request.PostCustomerRequest
import com.carollibrary.controllers.request.PutCustomerRequest
import com.carollibrary.controllers.response.CustomerResponse
import com.carollibrary.extension.toCustomerModel
import com.carollibrary.extension.toCustomerResponse
import com.carollibrary.security.UserCanOnlyAccessTheirOwnResource
import com.carollibrary.service.CustomerService
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/customers")
class CustomerController(
    private val customerService: CustomerService
) {
    @GetMapping
    @UserCanOnlyAccessTheirOwnResource
    fun getAllCustomers(@RequestParam name: String?, @PageableDefault(page = 0, size = 10) pageable: Pageable): List<CustomerResponse> {
         return customerService.getAllCustomers(name, pageable).map { it.toCustomerResponse() }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody @Valid customer: PostCustomerRequest) {
        customerService.create(customer.toCustomerModel())
    }

    @GetMapping("/{id}")
    @UserCanOnlyAccessTheirOwnResource
    fun getCustomers(@PathVariable id: Int): CustomerResponse {
        return customerService.findById(id).toCustomerResponse()
    }

    @PutMapping("/{id}")
    @UserCanOnlyAccessTheirOwnResource
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(@PathVariable id: Int, @RequestBody @Valid customer: PutCustomerRequest) {
        val customerSaved = customerService.findById(id)
        customerService.update(customer.toCustomerModel(customerSaved))
    }

   @DeleteMapping("/{id}")
   @UserCanOnlyAccessTheirOwnResource
   @ResponseStatus(HttpStatus.NO_CONTENT)
   fun delete(@PathVariable id: Int) {
       customerService.delete(id)
   }


}