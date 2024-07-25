package com.carollibrary.events

import com.carollibrary.models.PurchaseModel
import org.springframework.context.ApplicationEvent

class PurchaseEvent (
    source: Any,
    val purchaseModel: PurchaseModel
): ApplicationEvent(source)