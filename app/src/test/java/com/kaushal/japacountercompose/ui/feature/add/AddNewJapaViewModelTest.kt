package com.kaushal.japacountercompose.ui.feature.add

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isInstanceOf
import com.kaushal.japacountercompose.MainDispatcherExtension
import com.kaushal.japacountercompose.domain.Outcome
import com.kaushal.japacountercompose.fakes.FakeMainRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class AddNewJapaViewModelTest {

    @JvmField
    @RegisterExtension
    val mainDispatcherExtension = MainDispatcherExtension()

    private val repository = FakeMainRepository()
    private val viewModel = AddNewJapaViewModel(repository)

    @Test
    fun `addNewJapa success emits success`() = runTest(mainDispatcherExtension.dispatcher) {
        viewModel.addNewJapaOutcome.test {
            viewModel.addNewJapa("Hari", 1000)
            assertThat(awaitItem()).isEqualTo(Outcome.Success(Unit))
        }
    }

    @Test
    fun `addNewJapa failure emits failure`() = runTest(mainDispatcherExtension.dispatcher) {
        repository.addJapaError = RuntimeException("fail")

        viewModel.addNewJapaOutcome.test {
            viewModel.addNewJapa("Hari", 1000)
            val result = awaitItem()
            assertThat(result).isInstanceOf(Outcome.Failure::class)
            assertThat((result as Outcome.Failure).message).isEqualTo("fail")
        }
    }

    @Test
    fun `addNewJapa passes given args to repository`() = runTest(mainDispatcherExtension.dispatcher) {
        viewModel.addNewJapaOutcome.test {
            viewModel.addNewJapa("Hari", 1000)
            awaitItem()
        }
        assertThat(repository.addJapaCalls).containsExactly("Hari" to 1000)
    }

    @Test
    fun `addNewJapa null target passes null`() = runTest(mainDispatcherExtension.dispatcher) {
        viewModel.addNewJapaOutcome.test {
            viewModel.addNewJapa("Test", null)
            awaitItem()
        }
        assertThat(repository.addJapaCalls).containsExactly("Test" to null)
    }

    @Test
    fun `isLoading resets to false after success`() = runTest(mainDispatcherExtension.dispatcher) {
        viewModel.addNewJapaOutcome.test {
            viewModel.addNewJapa("Hari", 1000)
            awaitItem()
        }
        assertThat(viewModel.isLoading.value).isFalse()
    }

    @Test
    fun `isLoading resets to false after failure`() = runTest(mainDispatcherExtension.dispatcher) {
        repository.addJapaError = RuntimeException("fail")

        viewModel.addNewJapaOutcome.test {
            viewModel.addNewJapa("Hari", 1000)
            awaitItem()
        }
        assertThat(viewModel.isLoading.value).isFalse()
    }
}
