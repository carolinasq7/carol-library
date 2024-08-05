package com.carollibrary.events.listener

import com.carollibrary.events.PurchaseEvent
import com.carollibrary.helper.buildPurchase
import com.carollibrary.service.PurchaseService
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.UUID

@ExtendWith(MockKExtension::class)
class GenerateNfeListenerTest {

    @MockK
    private lateinit var purshaseService: PurchaseService

    @InjectMockKs
    private lateinit var generateNfeListener: GenerateNfeListener

    @Test
    fun`Should generate nfe` () {
        val fakeNfe = UUID.randomUUID()
        val purchase = buildPurchase(nfe = fakeNfe.toString())
        mockkStatic(UUID::class)

        every { UUID.randomUUID() } returns fakeNfe
        every { purshaseService.update(purchase) } just runs

        generateNfeListener.listen(PurchaseEvent(this, purchase))

        verify(exactly = 1) { purshaseService.update(purchase) }
    }
}