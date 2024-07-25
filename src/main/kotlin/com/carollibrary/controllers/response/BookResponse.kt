package com.carollibrary.controllers.response

import com.carollibrary.enums.BookStatus
import com.carollibrary.models.CustomerModel
import java.math.BigDecimal

data class BookResponse(
    var id: Int? = null,
    var name: String,
    var price: BigDecimal,
    var customer: CustomerModel? = null,
    var status: BookStatus? = null
)
