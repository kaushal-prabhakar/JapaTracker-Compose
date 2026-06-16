package com.kaushal.japacountercompose.ui

sealed interface StartDestinationState {
    data object Loading : StartDestinationState
    data class Ready(val route: String) : StartDestinationState
}