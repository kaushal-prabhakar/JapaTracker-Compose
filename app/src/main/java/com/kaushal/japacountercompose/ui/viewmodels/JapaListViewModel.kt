package com.kaushal.japacountercompose.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaushal.japacountercompose.data.JapaInfoEntities
import com.kaushal.japacountercompose.data.Outcome
import com.kaushal.japacountercompose.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class JapaListViewModel @Inject constructor(
    repository: MainRepository
) : ViewModel() {

    val japaListOutcome: StateFlow<Outcome<List<JapaInfoEntities>>> =
        repository.getJapaList()
            .map<List<JapaInfoEntities>, Outcome<List<JapaInfoEntities>>> { Outcome.Success(it) }
            .catch { emit(Outcome.Failure(it, it.message.orEmpty())) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = Outcome.Loading
            )
}
