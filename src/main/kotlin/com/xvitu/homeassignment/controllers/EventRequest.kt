package com.xvitu.homeassignment.controllers

import com.xvitu.homeassignment.OperationsType
import java.math.BigDecimal

data class EventRequest(
    val type: OperationsType,
    val amount: BigDecimal,
    val origin: String? = null,
    val destination: String? = null
)
