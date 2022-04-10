package com.xvitu.homeassignment

sealed class Error {
    data class OperationWithoutDestination(
        val message: String = "You must provide a destination to perform the operation"
    ): Error()

    data class OperationWithoutOrigin(
        val message: String = "You must provide a origin to perform the operation"
    ): Error()

    data class InsuficientBalanceOnAccount(
        val message: String = "the account does not have enough balance to carry out the operation"
    ): Error()

    object AccountDoesNotExist: Error()
}

