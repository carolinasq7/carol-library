package com.carollibrary.controllers.request

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class PutBookRequest (
    @field:NotEmpty
    var name: String?,

    @field:NotNull
    var price: BigDecimal?
)