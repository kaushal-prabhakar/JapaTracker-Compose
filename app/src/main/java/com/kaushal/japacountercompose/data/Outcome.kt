package com.kaushal.japacountercompose.data

sealed class Outcome {

    data class Success<out T>(val data: T, val isEmptyList: Boolean) : Outcome()

    data class Failure(
        val error: Throwable,
        val message: String,
        val showAsOverlay: Boolean
    ): Outcome()

    data class InProgress(val showOverlay: Boolean) : Outcome()

    companion object {

        fun loading(showOverlay: Boolean = false): Outcome = InProgress(showOverlay)

        fun <T> success(data: T, emptyList: Boolean = false): Outcome = Success(data, emptyList)

        fun failure(error: Throwable, message: String, showAsOverlay: Boolean = false): Outcome = Failure(error, message, showAsOverlay)
    }

}