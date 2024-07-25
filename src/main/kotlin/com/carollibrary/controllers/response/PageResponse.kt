package com.carollibrary.controllers.response

class PageResponse<T>(
    var items: List<T>,
    var currentPage: Int,
    var totalItems: Long,
    var totalPage: Int
)