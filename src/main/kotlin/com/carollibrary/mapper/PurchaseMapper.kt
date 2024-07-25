package com.carollibrary.mapper

import com.carollibrary.controllers.request.PostPurchaseRequest
import com.carollibrary.models.PurchaseModel
import com.carollibrary.service.BookService
import com.carollibrary.service.CustomerService
import org.springframework.stereotype.Component

@Component
class PurchaseMapper(
    private val bookService: BookService,
    private val customerService: CustomerService
) {

    fun toModel(request: PostPurchaseRequest): PurchaseModel {
        val customer = customerService.findById(request.customerId)
        val books = bookService.findAllByIds(request.bookIds)

        return PurchaseModel(
            customer = customer,
            books = books.toMutableList(),
            price = books.sumOf { it.price }
        )
    }
}
