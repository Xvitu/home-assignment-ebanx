package com.xvitu.homeassignment

import java.math.BigDecimal

data class Account(val id: String, var balance: BigDecimal = BigDecimal.ZERO) {

    fun deposit(amount: BigDecimal) { balance += amount }

    fun withdraw(amount: BigDecimal) { balance -= amount }
}
