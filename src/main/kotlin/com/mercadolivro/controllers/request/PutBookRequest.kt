package com.mercadolivro.controllers.request

import com.fasterxml.jackson.annotation.JsonAlias
import com.mercadolivro.enums.BookStatus
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class PutBookRequest (
    @field:NotEmpty
    var name: String?,

    @field:NotNull
    var price: BigDecimal?
)