package com.mercadolivro.models

import com.mercadolivro.enums.BookStatus
import jakarta.persistence.*
import org.hibernate.sql.results.internal.RowTransformerConstructorImpl
import org.yaml.snakeyaml.constructor.Constructor
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

    @Column
    @Enumerated(EnumType.STRING)
    var status: BookStatus? = null,

    @ManyToOne
    @JoinColumn(name = "customer_id")
    var customer: CustomerModel? = null
)
