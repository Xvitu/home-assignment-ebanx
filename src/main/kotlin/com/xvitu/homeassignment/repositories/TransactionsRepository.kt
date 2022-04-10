package com.xvitu.homeassignment.repositories

import com.xvitu.homeassignment.Transaction
import org.springframework.stereotype.Component

@Component
class TransactionsRepository {
    private var transactions = mutableListOf<Transaction>()

    fun deleteAll() {
        transactions = mutableListOf()
    }

    fun add(transaction: Transaction) = transactions.add(transaction)
}
