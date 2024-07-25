package com.carollibrary.models

import com.carollibrary.enums.BookStatus
import com.carollibrary.enums.BookStatus.CANCELED
import com.carollibrary.enums.Errors
import com.carollibrary.exception.BadRequestException
import jakarta.persistence.*
import java.math.BigDecimal

@Entity(name = "book")
data class BookModel (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @Column
    var name: String,

    @Column
    var price: BigDecimal,

    @ManyToOne
    @JoinColumn(name = "customer_id")
    var customer: CustomerModel? = null
) {
    @Column
    @Enumerated(EnumType.STRING)
    var status: BookStatus? = null
        set(value)  {
            if(field == BookStatus.DELETED || field == CANCELED)
                throw BadRequestException (Errors.ML102.message.format(field), Errors.ML102.code)

            field = value
        }
    constructor(id: Int? = null,
                name: String,
                price: BigDecimal,
                customer: CustomerModel? = null,
                status: BookStatus?): this(id, name, price, customer) {
        this.status = status
    }

}
