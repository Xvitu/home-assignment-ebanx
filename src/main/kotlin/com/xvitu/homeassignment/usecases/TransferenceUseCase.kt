package com.xvitu.homeassignment.usecases

import arrow.core.Either
import com.xvitu.homeassignment.Account
import org.springframework.stereotype.Component
import java.math.BigDecimal
import com.xvitu.homeassignment.Error
import com.xvitu.homeassignment.OperationsType
import com.xvitu.homeassignment.Transaction
import com.xvitu.homeassignment.repositories.AccountsRepository
import com.xvitu.homeassignment.repositories.TransactionsRepository

@Component
class TransferenceUseCase(
    val accountsRepository: AccountsRepository, val transactionsRepository: TransactionsRepository
) {
    fun execute(dto: TransferenceDto): Either<Error, OperationResult> {

        if (dto.origin == null) {
            return Either.Left(Error.OperationWithoutOrigin())
        }

        if (dto.destination == null) {
            return Either.Left(Error.OperationWithoutDestination())
        }

        val originAccount = accountsRepository.find(dto.origin) ?: return Either.Left(Error.AccountDoesNotExist)

        if (originAccount.balance < dto.amount) {
            return Either.Left(Error.InsuficientBalanceOnAccount())
        }

        val destinationAccount = accountsRepository.find(dto.destination)
            ?: Account(dto.destination)

        originAccount.withdraw(dto.amount)
        accountsRepository.persist(originAccount)

        destinationAccount.deposit(dto.amount)
        accountsRepository.persist(destinationAccount)

        val transaction = Transaction(OperationsType.transfer.toString(), destinationAccount.id, originAccount.id)
        transactionsRepository.add(transaction)

        return Either.Right(OperationResult(destination = destinationAccount, origin = originAccount))
    }
}

data class TransferenceDto(val origin: String?, val destination: String?, val amount: BigDecimal)
