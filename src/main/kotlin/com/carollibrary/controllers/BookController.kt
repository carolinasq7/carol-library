package com.carollibrary.controllers

import com.carollibrary.controllers.request.PostBookRequest
import com.carollibrary.controllers.request.PutBookRequest
import com.carollibrary.controllers.response.BookResponse
import com.carollibrary.controllers.response.PageResponse
import com.carollibrary.extension.toBookModel
import com.carollibrary.extension.toBookResponse
import com.carollibrary.extension.toPageResponse
import com.carollibrary.security.UserCanOnlyAccessTheirOwnResource
import com.carollibrary.service.BookService
import com.carollibrary.service.CustomerService
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/books")
class BookController(
    private val bookService: BookService,
    private val customerService: CustomerService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody @Valid request: PostBookRequest) {
        val customer = customerService.findById(request.customerId)
        bookService.create(request.toBookModel(customer))
    }

    @GetMapping
    fun findAll(@PageableDefault(page = 0, size = 10) pageable: Pageable): PageResponse<BookResponse> {
        return bookService.findAll(pageable).map { it.toBookResponse() }.toPageResponse()
    }

    @GetMapping("/active")
    fun findActives(@PageableDefault(page = 0, size = 10) pageable: Pageable): PageResponse<BookResponse> {
        return bookService.findActives(pageable).map { it.toBookResponse() }.toPageResponse()
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Int): BookResponse {
        return bookService.findById(id).toBookResponse()
    }

    @DeleteMapping("/{id}")
    @UserCanOnlyAccessTheirOwnResource
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteById(@PathVariable id: Int) {
        bookService.delete(id)
    }

    @PutMapping("/{id}")
    @UserCanOnlyAccessTheirOwnResource
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(@PathVariable id: Int, @RequestBody book : PutBookRequest) {
        val bookSaved = bookService.findById(id)
        bookService.update(book.toBookModel(bookSaved))
    }

}