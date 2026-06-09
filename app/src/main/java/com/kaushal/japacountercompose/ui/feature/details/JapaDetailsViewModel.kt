package com.kaushal.japacountercompose.ui.feature.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaushal.japacountercompose.domain.JapaInfoEntities
import com.kaushal.japacountercompose.domain.MainRepository
import com.kaushal.japacountercompose.domain.Outcome
import com.kaushal.japacountercompose.domain.UpdateType
import com.kaushal.japacountercompose.ui.JapaAppScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface JapaDetailsAction {
    data object UpdateSuccess : JapaDetailsAction
    data object DeleteSuccess : JapaDetailsAction
    data class Failure(val message: String) : JapaDetailsAction
}

@HiltViewModel
class JapaDetailsViewModel @Inject constructor(
    private val repository: MainRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val japaId: Int = savedStateHandle[JapaAppScreens.JAPA_ID_ARG]
        ?: throw IllegalArgumentException("japaId argument is required for JapaDetailsScreen")

    val japaDetailOutcome: StateFlow<Outcome<JapaInfoEntities>> =
        repository.getJapaById(japaId)
            .filterNotNull()
            .map<JapaInfoEntities, Outcome<JapaInfoEntities>> { Outcome.Success(it) }
            .catch { emit(Outcome.Failure(it, it.message.orEmpty())) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = Outcome.Loading
            )

    private val _actionEvent = MutableSharedFlow<JapaDetailsAction>(0)
    val actionEvent = _actionEvent.asSharedFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    /** Adds the given value to the current count. */
    fun incrementCount(value: Int) {
        val current = currentCount() ?: return
        launchAction(JapaDetailsAction.UpdateSuccess) {
            repository.updateCount(japaId, current + value, value, UpdateType.INCREMENT)
        }
    }

    /** Subtracts the given value from the current count, floored at zero. */
    fun decrementCount(value: Int) {
        val current = currentCount() ?: return
        val newCount = (current - value).coerceAtLeast(0)
        launchAction(JapaDetailsAction.UpdateSuccess) {
            repository.updateCount(japaId, newCount, value, UpdateType.DECREMENT)
        }
    }

    /** Marks this japa as completed. */
    fun markComplete() = launchAction(JapaDetailsAction.UpdateSuccess) {
        repository.markComplete(japaId)
    }

    /** Resets this japa's count back to zero. */
    fun resetCounter() = launchAction(JapaDetailsAction.UpdateSuccess) {
        repository.resetCounter(japaId)
    }

    /** Deletes this japa permanently. */
    fun deleteJapa() = launchAction(JapaDetailsAction.DeleteSuccess) {
        repository.deleteJapa(japaId)
    }

    private fun currentCount(): Int? =
        (japaDetailOutcome.value as? Outcome.Success)?.data?.currentCount

    private fun launchAction(successEvent: JapaDetailsAction, block: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                block()
                _actionEvent.emit(successEvent)
            } catch (ex: Exception) {
                _actionEvent.emit(JapaDetailsAction.Failure(ex.message.orEmpty()))
            } finally {
                _isLoading.value = false
            }
        }
    }
}
