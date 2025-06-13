package com.kaushal.japacountercompose.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaushal.japacountercompose.data.JapaInfoEntities
import com.kaushal.japacountercompose.data.JapaStatus
import com.kaushal.japacountercompose.data.Outcome
import com.kaushal.japacountercompose.data.UpdateType
import com.kaushal.japacountercompose.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class AddNewJapaViewModel @Inject constructor(private val repository: MainRepository) :
    ViewModel() {

    // using shared view because every time when the screen appears we don't need the latest state flow value
    private val _addNewJapaOutcome = MutableSharedFlow<Outcome>(0)
    val addNewJapaOutcome = _addNewJapaOutcome.asSharedFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun addNewJapa(name: String, target: Int?) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val entities = JapaInfoEntities(
                    name = name,
                    target = target ?: 0,
                    status = JapaStatus.ACTIVE,
                    currentCount = 0,
                    lastUpdatedValue = 0,
                    lastUpdatedType = UpdateType.INCREMENT,
                    lastUpdatedTime = LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm"))
                )
                _addNewJapaOutcome.emit(Outcome.loading())
                repository.addNewJapa(japaEntities = entities)
                _addNewJapaOutcome.emit(Outcome.success(true))
            } catch (ex: Exception) {
                _addNewJapaOutcome.emit(Outcome.failure(ex, ex.message.orEmpty()))
            } finally {
                _isLoading.value = false
            }
        }
    }
}