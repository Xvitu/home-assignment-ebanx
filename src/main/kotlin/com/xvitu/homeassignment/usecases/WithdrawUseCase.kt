package com.xvitu.homeassignment.usecases

import arrow.core.Either
import org.springframework.stereotype.Component
import java.math.BigDecimal
import com.xvitu.homeassignment.Error
import com.xvitu.homeassignment.OperationsType
import com.xvitu.homeassignment.Transaction
import com.xvitu.homeassignment.repositories.AccountsRepository
import com.xvitu.homeassignment.repositories.TransactionsRepository

@Component
class WithdrawUseCase(val accountsRepository: AccountsRepository, val transactionsRepository: TransactionsRepository) {
    fun execute(dto: WithdrawDto): Either<Error, OperationResult> {
        if (dto.origin == null) {
            return Either.Left(Error.OperationWithoutOrigin())
        }

        val account = accountsRepository.find(dto.origin) ?: return Either.Left(Error.AccountDoesNotExist)

        if (account.balance < dto.amount) {
            return Either.Left(Error.InsuficientBalanceOnAccount())
        }

        account.withdraw(dto.amount)
        accountsRepository.persist(account)

        val transaction = Transaction(OperationsType.withdraw.toString(), null, dto.origin)
        transactionsRepository.add(transaction)

        return Either.Right(OperationResult(destination = null, origin = account))
    }

}

data class WithdrawDto(val origin: String?, val amount: BigDecimal)
