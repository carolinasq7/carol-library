package com.carollibrary.controllers

import com.carollibrary.controllers.request.PostPurchaseRequest
import com.carollibrary.mapper.PurchaseMapper
import com.carollibrary.security.UserCanOnlyAccessTheirOwnResource
import com.carollibrary.service.PurchaseService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("purchases")
class PurchaseController(
    private val purchaseService: PurchaseService,
    private val purchaseMapper: PurchaseMapper
) {

    @PostMapping
    @UserCanOnlyAccessTheirOwnResource
    @ResponseStatus(HttpStatus.CREATED)
    fun purchase(@RequestBody request: PostPurchaseRequest) {
        purchaseService.create(purchaseMapper.toModel(request))

    }

}