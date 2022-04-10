package com.xvitu.homeassignment.usecases

import com.fasterxml.jackson.annotation.JsonInclude
import com.xvitu.homeassignment.Account

@JsonInclude(JsonInclude.Include.NON_NULL)
data class OperationResult(val origin: Account?, val destination: Account?)
