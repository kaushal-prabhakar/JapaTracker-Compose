package com.kaushal.japacountercompose.data

sealed class Outcome<out T> {

    data class Success<out T>(val data: T, val isEmptyList: Boolean) : Outcome<T>()

    data class Failure(
        val error: Throwable,
        val message: String,
        val showAsOverlay: Boolean
    ): Outcome<Nothing>()

    data class InProgress(val showOverlay: Boolean) : Outcome<Nothing>()

    companion object {

        fun loading(showOverlay: Boolean = false): Outcome<Nothing> = InProgress(showOverlay)

        fun <T> success(data: T, emptyList: Boolean = false): Outcome<T> = Success(data, emptyList)

        fun failure(error: Throwable, message: String, showAsOverlay: Boolean = false): Outcome<Nothing> = Failure(error, message, showAsOverlay)
    }

}