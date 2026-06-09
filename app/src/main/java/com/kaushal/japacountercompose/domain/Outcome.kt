package com.kaushal.japacountercompose.domain

sealed class Outcome<out T> {

    data class Success<out T>(val data: T) : Outcome<T>()

    data class Failure(
        val error: Throwable,
        val message: String
    ) : Outcome<Nothing>()

    data object Loading : Outcome<Nothing>()

}