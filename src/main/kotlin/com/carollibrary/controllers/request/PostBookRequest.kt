package com.carollibrary.controllers.request

import com.fasterxml.jackson.annotation.JsonAlias
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class PostBookRequest (
    @field:NotEmpty
    var name: String,

    @field:NotNull
    var price: BigDecimal,

    @JsonAlias("customer_id")
    var customerId: Int,

)