package com.kaushal.japacountercompose.ui.feature.list

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import com.kaushal.japacountercompose.MainDispatcherExtension
import com.kaushal.japacountercompose.domain.JapaInfoEntities
import com.kaushal.japacountercompose.domain.JapaStatus
import com.kaushal.japacountercompose.domain.Outcome
import com.kaushal.japacountercompose.domain.UpdateType
import com.kaushal.japacountercompose.fakes.FakeMainRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class JapaListViewModelTest {

    @JvmField
    @RegisterExtension
    val mainDispatcherExtension = MainDispatcherExtension()

    private val repository = FakeMainRepository()

    private fun makeJapa(id: Int = 1) = JapaInfoEntities(
        id = id,
        name = "Hari Krishna",
        target = 108,
        status = JapaStatus.ACTIVE,
        currentCount = 10,
        lastUpdatedValue = 1,
        lastUpdatedType = UpdateType.INCREMENT,
        lastUpdatedTime = "15 Mar 2024 • 02:30 PM"
    )

    @Test
    fun `initial state before repository responds is loading`() = runTest(mainDispatcherExtension.dispatcher) {
        val viewModel = JapaListViewModel(repository)

        viewModel.japaListOutcome.test {
            assertThat(awaitItem()).isEqualTo(Outcome.Loading)
        }
    }

    @Test
    fun `repository emits list, state becomes success`() = runTest(mainDispatcherExtension.dispatcher) {
        val viewModel = JapaListViewModel(repository)
        val japas = listOf(makeJapa())

        viewModel.japaListOutcome.test {
            assertThat(awaitItem()).isEqualTo(Outcome.Loading)
            repository.japaListFlow.value = japas
            assertThat(awaitItem()).isEqualTo(Outcome.Success(japas))
        }
    }

    @Test
    fun `repository emits empty list, state is success with empty list`() = runTest(mainDispatcherExtension.dispatcher) {
        val viewModel = JapaListViewModel(repository)

        viewModel.japaListOutcome.test {
            assertThat(awaitItem()).isEqualTo(Outcome.Loading)
            repository.japaListFlow.value = emptyList()
            assertThat(awaitItem()).isEqualTo(Outcome.Success(emptyList()))
        }
    }

    @Test
    fun `repository error, state is failure`() = runTest(mainDispatcherExtension.dispatcher) {
        repository.getJapaListError = RuntimeException("DB error")
        val viewModel = JapaListViewModel(repository)

        viewModel.japaListOutcome.test {
            assertThat(awaitItem()).isEqualTo(Outcome.Loading)
            val failure = awaitItem()
            assertThat(failure).isInstanceOf(Outcome.Failure::class)
            assertThat((failure as Outcome.Failure).message).isEqualTo("DB error")
        }
    }
}
