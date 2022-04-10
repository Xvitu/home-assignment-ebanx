package com.xvitu.homeassignment.controllers

import com.xvitu.homeassignment.repositories.AccountsRepository
import com.xvitu.homeassignment.repositories.TransactionsRepository
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AdminController(
    private val transactionsRepository: TransactionsRepository, private val accountsRepository: AccountsRepository
) {
    @PostMapping("/reset", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun resetApplication(): ResponseEntity<String> {
        transactionsRepository.deleteAll()
        accountsRepository.deleteAll()

        return ResponseEntity.ok("OK")
    }
}
