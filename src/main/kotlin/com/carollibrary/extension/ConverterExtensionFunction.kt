package com.carollibrary.extension

import com.carollibrary.controllers.request.PostBookRequest
import com.carollibrary.controllers.request.PostCustomerRequest
import com.carollibrary.controllers.request.PutBookRequest
import com.carollibrary.controllers.request.PutCustomerRequest
import com.carollibrary.controllers.response.BookResponse
import com.carollibrary.controllers.response.CustomerResponse
import com.carollibrary.controllers.response.PageResponse
import com.carollibrary.enums.CustomerStatus
import com.carollibrary.models.BookModel
import com.carollibrary.models.CustomerModel
import org.springframework.data.domain.Page
import com.carollibrary.enums.BookStatus as BookStatus1

fun PostCustomerRequest.toCustomerModel() : CustomerModel {
    return CustomerModel(
        name = this.name,
        email = this.email,
        status = CustomerStatus.ACTIVE,
        password = this.password)
}

fun PutCustomerRequest.toCustomerModel(previousValue: CustomerModel): CustomerModel {
    return CustomerModel(id = previousValue.id,
        name = this.name,
        email = this.email,
        status = previousValue.status,
        password = previousValue.password
    )

}

fun PostBookRequest.toBookModel(customer: CustomerModel): BookModel {
    return BookModel(
        name = this.name,
        price = this.price,
        status = BookStatus1.ACTIVE,
        customer = customer
    )
}

fun PutBookRequest.toBookModel(previousValue: BookModel): BookModel {
    return BookModel(
        id = previousValue.id,
        name = this.name ?: previousValue.name,
        price = this.price ?: previousValue.price,
        status = previousValue.status,
        customer = previousValue.customer
    )
}

fun CustomerModel.toCustomerResponse(): CustomerResponse {
    return CustomerResponse(
        id = this.id,
        name = this.name,
        email = this.email,
        status = this.status
    )
}

fun BookModel.toBookResponse(): BookResponse {
    return BookResponse(
        id = this.id,
        name = this.name,
        price = this.price,
        customer = this.customer,
        status = this.status
    )
}

fun <T> Page<T>.toPageResponse(): PageResponse<T> {
    return PageResponse(this.content, this.number, this.totalElements, this.totalPages)
}
