package com.xvitu.homeassignment.repositories

import com.xvitu.homeassignment.Account
import org.springframework.stereotype.Component

@Component
class AccountsRepository {

    private var accounts = mutableListOf<Account>()

    fun deleteAll() {
        accounts = mutableListOf()
    }

    fun find(id: String) = accounts.find { it.id == id }

    fun persist(account: Account) = accounts.add(account)
}
