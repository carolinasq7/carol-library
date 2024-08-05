package com.carollibrary.helper

import com.carollibrary.enums.CustomerStatus
import com.carollibrary.enums.Role
import com.carollibrary.models.BookModel
import com.carollibrary.models.CustomerModel
import com.carollibrary.models.PurchaseModel
import java.math.BigDecimal
import java.util.*

fun buildCustomer(
    id: Int? = null,
    name: String = "customer name",
    email: String = "${UUID.randomUUID()}@email.com",
    password: String = "password"
) = CustomerModel(
    id = id,
    name = name,
    email = email,
    status = CustomerStatus.ACTIVE,
    password = password,
    roles = setOf(Role.CUSTOMER)
)

fun buildPurchase(
    id: Int? = null,
    customer: CustomerModel = buildCustomer(),
    books: MutableList<BookModel> = mutableListOf<BookModel>(),
    nfe: String = UUID.randomUUID().toString(),
    price: BigDecimal = BigDecimal.TEN
) = PurchaseModel(
    id = id,
    customer = customer,
    books = books,
    nfe = nfe,
    price = price
)