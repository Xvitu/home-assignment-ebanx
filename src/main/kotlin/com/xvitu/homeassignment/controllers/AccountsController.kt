package com.xvitu.homeassignment.controllers

import com.xvitu.homeassignment.Error
import com.xvitu.homeassignment.repositories.AccountsRepository
import com.xvitu.homeassignment.OperationsType
import com.xvitu.homeassignment.usecases.DepositDTO
import com.xvitu.homeassignment.usecases.DepositUseCase
import com.xvitu.homeassignment.usecases.TransferenceDto
import com.xvitu.homeassignment.usecases.TransferenceUseCase
import com.xvitu.homeassignment.usecases.WithdrawDto
import com.xvitu.homeassignment.usecases.WithdrawUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RestController
class AccountsController(
    private val accountsRepository: AccountsRepository,
    private val depositUseCase: DepositUseCase,
    private val transferenceUseCase: TransferenceUseCase,
    private val withdrawUseCase: WithdrawUseCase
) {

    @GetMapping("/balance", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun balance(@RequestParam("account_id") accountId: String): ResponseEntity<BigDecimal> {
        val account = accountsRepository.find(accountId)

        if (account === null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BigDecimal.ZERO)
        }

        return ResponseEntity.ok(account.balance)
    }

    @PostMapping("/event", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun event(@Validated @RequestBody payload: EventRequest): Any {
        val result = when(payload.type) {
            OperationsType.deposit -> depositUseCase.handle(DepositDTO(payload.destination, payload.amount))
            OperationsType.transfer -> transferenceUseCase.execute(
                TransferenceDto(payload.origin, payload.destination, payload.amount)
            )
            OperationsType.withdraw -> withdrawUseCase.execute(WithdrawDto(payload.origin, payload.amount))
        }

        return result.fold(
            ifRight = { ResponseEntity.status(HttpStatus.CREATED).body(it) },
            ifLeft = {
                when(it) {
                    is Error.OperationWithoutDestination ->
                        ResponseEntity.status(HttpStatus.CONFLICT).body(it.message)
                    is Error.AccountDoesNotExist -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(0)
                    is Error.OperationWithoutOrigin -> ResponseEntity.status(HttpStatus.CONFLICT).body(it.message)
                    is Error.InsuficientBalanceOnAccount ->
                        ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(it.message)
                }
            }
        )
    }
}
