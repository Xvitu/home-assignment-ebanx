package com.xvitu.homeassignment.usecases

import arrow.core.Either
import com.xvitu.homeassignment.Account
import com.xvitu.homeassignment.Error
import com.xvitu.homeassignment.OperationsType
import com.xvitu.homeassignment.Transaction
import com.xvitu.homeassignment.repositories.AccountsRepository
import com.xvitu.homeassignment.repositories.TransactionsRepository
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class DepositUseCase(
    private val accountsRepository: AccountsRepository, private val transactionsRepository: TransactionsRepository
) {
    fun handle(dto: DepositDTO): Either<Error, OperationResult> {
        if (dto.destination == null) {
            return Either.Left(Error.OperationWithoutDestination())
        }

        val account = accountsRepository.find(dto.destination) ?: Account(dto.destination)
        account.deposit(dto.amount)
        accountsRepository.persist(account)

        val transaction = Transaction(OperationsType.deposit.toString(), dto.destination)
        transactionsRepository.add(transaction)

        return Either.Right(OperationResult(destination = account, origin = null))
    }
}

data class DepositDTO(val destination: String?, val amount: BigDecimal)
