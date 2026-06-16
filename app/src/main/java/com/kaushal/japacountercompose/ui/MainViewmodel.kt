package com.kaushal.japacountercompose.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaushal.japacountercompose.domain.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewmodel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {

    val startDestination: StateFlow<StartDestinationState> =
        mainRepository.getJapaList()
            .map { list ->
                if (list.isEmpty()) StartDestinationState.Ready(JapaAppScreens.welcome.name)
                else StartDestinationState.Ready(JapaAppScreens.japaList.name)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = StartDestinationState.Loading
            )
}