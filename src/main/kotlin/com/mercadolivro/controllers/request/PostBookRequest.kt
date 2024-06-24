package com.mercadolivro.controllers.request

import com.fasterxml.jackson.annotation.JsonAlias
import com.mercadolivro.enums.BookStatus
import java.math.BigDecimal

data class PostBookRequest (
    var name: String,
    var price: BigDecimal,

    @JsonAlias("customer_id")
    var customerId: Int,

)