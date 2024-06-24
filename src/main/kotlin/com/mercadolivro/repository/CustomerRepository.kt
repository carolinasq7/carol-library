package com.mercadolivro.repository

import com.mercadolivro.models.CustomerModel
import org.springframework.data.repository.CrudRepository

interface CustomerRepository : CrudRepository<CustomerModel, Int> {

    fun findByNameContaining (name: String): List<CustomerModel>

}