package com.kaushal.japacountercompose.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaushal.japacountercompose.data.Outcome
import com.kaushal.japacountercompose.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JapaListViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _japaListOutcome = MutableStateFlow(Outcome.loading())
    val japaListOutcome = _japaListOutcome.asStateFlow()

    fun getMyJapaList() {
        viewModelScope.launch {
            try {
                _japaListOutcome.emit(Outcome.InProgress(true))
                val result = repository.getMyJapaList()
                _japaListOutcome.emit(Outcome.success(result))
            } catch (ex: Exception) {
                _japaListOutcome.emit(Outcome.failure(ex, ex.message.orEmpty()))
            }
        }
    }

}