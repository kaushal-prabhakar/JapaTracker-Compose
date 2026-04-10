package com.kaushal.japacountercompose.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaushal.japacountercompose.data.Outcome
import com.kaushal.japacountercompose.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNewJapaViewModel @Inject constructor(private val repository: MainRepository) :
    ViewModel() {

    private val _addNewJapaOutcome = MutableSharedFlow<Outcome<Unit>>(0)
    val addNewJapaOutcome = _addNewJapaOutcome.asSharedFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    /** Creates a new japa with the given name and optional target. */
    fun addNewJapa(name: String, target: Int?) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.addJapa(name, target)
                _addNewJapaOutcome.emit(Outcome.Success(Unit))
            } catch (ex: Exception) {
                _addNewJapaOutcome.emit(Outcome.Failure(ex, ex.message.orEmpty()))
            } finally {
                _isLoading.value = false
            }
        }
    }
}
