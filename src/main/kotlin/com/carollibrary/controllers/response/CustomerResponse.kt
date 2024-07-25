package com.carollibrary.controllers.response

import com.carollibrary.enums.CustomerStatus

data class CustomerResponse(
    var id: Int? = null,
    var name: String,
    var email: String,
    var status: CustomerStatus? = null
)