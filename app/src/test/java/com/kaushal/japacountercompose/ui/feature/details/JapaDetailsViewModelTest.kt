package com.kaushal.japacountercompose.ui.feature.details

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isInstanceOf
import com.kaushal.japacountercompose.MainDispatcherExtension
import com.kaushal.japacountercompose.domain.JapaInfoEntities
import com.kaushal.japacountercompose.domain.JapaStatus
import com.kaushal.japacountercompose.domain.Outcome
import com.kaushal.japacountercompose.domain.UpdateType
import com.kaushal.japacountercompose.fakes.FakeMainRepository
import com.kaushal.japacountercompose.fakes.UpdateCountCall
import com.kaushal.japacountercompose.ui.JapaAppScreens
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class JapaDetailsViewModelTest {

    @JvmField
    @RegisterExtension
    val mainDispatcherExtension = MainDispatcherExtension()

    private val repository = FakeMainRepository()

    private fun createViewModel(japaId: Int = 1) =
        JapaDetailsViewModel(repository, SavedStateHandle(mapOf(JapaAppScreens.JAPA_ID_ARG to japaId)))

    private fun makeJapa(
        id: Int = 1,
        target: Int? = 108,
        currentCount: Int = 0,
        status: JapaStatus = JapaStatus.ACTIVE
    ) = JapaInfoEntities(
        id = id,
        name = "Hari Krishna",
        target = target,
        status = status,
        currentCount = currentCount,
        lastUpdatedValue = 0,
        lastUpdatedType = UpdateType.INCREMENT,
        lastUpdatedTime = "15 Mar 2024 • 02:30 PM"
    )

    /** Collects [JapaDetailsViewModel.japaDetailOutcome] until it becomes [Outcome.Success] with [japa]. */
    private suspend fun primeState(viewModel: JapaDetailsViewModel, japaId: Int, japa: JapaInfoEntities) {
        viewModel.japaDetailOutcome.test {
            assertThat(awaitItem()).isEqualTo(Outcome.Loading)
            repository.japaByIdFlows.getOrPut(japaId) { MutableStateFlow(null) }.value = japa
            assertThat(awaitItem()).isEqualTo(Outcome.Success(japa))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `missing japaId throws`() {
        assertFailure { JapaDetailsViewModel(repository, SavedStateHandle(emptyMap())) }
            .isInstanceOf(IllegalArgumentException::class)
    }

    @Test
    fun `initial state is loading`() = runTest(mainDispatcherExtension.dispatcher) {
        val viewModel = createViewModel()

        viewModel.japaDetailOutcome.test {
            assertThat(awaitItem()).isEqualTo(Outcome.Loading)
        }
    }

    @Test
    fun `repository emits entity, state is success`() = runTest(mainDispatcherExtension.dispatcher) {
        val viewModel = createViewModel()
        primeState(viewModel, japaId = 1, japa = makeJapa())
    }

    @Test
    fun `repository error, state is failure`() = runTest(mainDispatcherExtension.dispatcher) {
        repository.getJapaByIdError = RuntimeException("DB error")
        val viewModel = createViewModel()

        viewModel.japaDetailOutcome.test {
            assertThat(awaitItem()).isEqualTo(Outcome.Loading)
            val failure = awaitItem()
            assertThat(failure).isInstanceOf(Outcome.Failure::class)
            assertThat((failure as Outcome.Failure).message).isEqualTo("DB error")
        }
    }

    @Test
    fun `increment when not success does nothing`() = runTest(mainDispatcherExtension.dispatcher) {
        val viewModel = createViewModel()

        viewModel.incrementCount(3)

        assertThat(repository.updateCountCalls).isEmpty()
    }

    @Test
    fun `increment adds value to count`() = runTest(mainDispatcherExtension.dispatcher) {
        val viewModel = createViewModel()
        primeState(viewModel, japaId = 1, japa = makeJapa(currentCount = 5))

        viewModel.actionEvent.test {
            viewModel.incrementCount(3)
            assertThat(awaitItem()).isEqualTo(JapaDetailsAction.UpdateSuccess)
        }
        assertThat(repository.updateCountCalls).containsExactly(UpdateCountCall(1, 8, 3, UpdateType.INCREMENT))
    }

    @Test
    fun `increment emits update success`() = runTest(mainDispatcherExtension.dispatcher) {
        val viewModel = createViewModel()
        primeState(viewModel, japaId = 1, japa = makeJapa(currentCount = 5))

        viewModel.actionEvent.test {
            viewModel.incrementCount(3)
            assertThat(awaitItem()).isEqualTo(JapaDetailsAction.UpdateSuccess)
        }
    }

    @Test
    fun `increment auto-completes at target`() = runTest(mainDispatcherExtension.dispatcher) {
        val viewModel = createViewModel()
        primeState(viewModel, japaId = 1, japa = makeJapa(currentCount = 107, target = 108, status = JapaStatus.ACTIVE))

        viewModel.actionEvent.test {
            viewModel.incrementCount(1)
            awaitItem()
        }
        assertThat(repository.updateCountCalls).containsExactly(UpdateCountCall(1, 108, 1, UpdateType.INCREMENT))
        assertThat(repository.markCompleteCalls).containsExactly(1)
    }

    @Test
    fun `increment auto-complete emits completion success`() = runTest(mainDispatcherExtension.dispatcher) {
        val viewModel = createViewModel()
        primeState(viewModel, japaId = 1, japa = makeJapa(currentCount = 107, target = 108, status = JapaStatus.ACTIVE))

        viewModel.actionEvent.test {
            viewModel.incrementCount(1)
            assertThat(awaitItem()).isEqualTo(JapaDetailsAction.CompletionSuccess)
        }
    }

    @Test
    fun `increment past target still auto-completes`() = runTest(mainDispatcherExtension.dispatcher) {
        val viewModel = createViewModel()
        primeState(viewModel, japaId = 1, japa = makeJapa(currentCount = 107, target = 108, status = JapaStatus.ACTIVE))

        viewModel.actionEvent.test {
            viewModel.incrementCount(5)
            awaitItem()
        }
        assertThat(repository.updateCountCalls).containsExactly(UpdateCountCall(1, 112, 5, UpdateType.INCREMENT))
        assertThat(repository.markCompleteCalls).containsExactly(1)
    }

    @Test
    fun `increment with null target never auto-completes`() = runTest(mainDispatcherExtension.dispatcher) {
        val viewModel = createViewModel()
        primeState(viewModel, japaId = 1, japa = makeJapa(currentCount = 100, target = null, status = JapaStatus.ACTIVE))

        viewModel.actionEvent.test {
            viewModel.incrementCount(1)
            assertThat(awaitItem()).isEqualTo(JapaDetailsAction.UpdateSuccess)
        }
        assertThat(repository.markCompleteCalls).isEmpty()
    }

    @Test
    fun `increment when already completed does not re-complete`() = runTest(mainDispatcherExtension.dispatcher) {
        val viewModel = createViewModel()
        primeState(viewModel, japaId = 1, japa = makeJapa(currentCount = 108, target = 108, status = JapaStatus.COMPLETED))

        viewModel.actionEvent.test {
            viewModel.incrementCount(1)
            assertThat(awaitItem()).isEqualTo(JapaDetailsAction.UpdateSuccess)
        }
        assertThat(repository.markCompleteCalls).isEmpty()
    }

    @Test
    fun `increment repository error emits failure`() = runTest(mainDispatcherExtension.dispatcher) {
        repository.updateCountError = RuntimeException("update failed")
        val viewModel = createViewModel()
        primeState(viewModel, japaId = 1, japa = makeJapa(currentCount = 5))

        viewModel.actionEvent.test {
            viewModel.incrementCount(1)
            val result = awaitItem()
            assertThat(result).isInstanceOf(JapaDetailsAction.Failure::class)
            assertThat((result as JapaDetailsAction.Failure).message).isEqualTo("update failed")
        }
    }

    @Test
    fun `decrement when not success does nothing`() = runTest(mainDispatcherExtension.dispatcher) {
        val viewModel = createViewModel()

        viewModel.decrementCount(3)

        assertThat(repository.updateCountCalls).isEmpty()
    }

    @Test
    fun `decrement subtracts value`() = runTest(mainDispatcherExtension.dispatcher) {
        val viewModel = createViewModel()
        primeState(viewModel, japaId = 1, japa = makeJapa(currentCount = 10))

        viewModel.actionEvent.test {
            viewModel.decrementCount(3)
            awaitItem()
        }
        assertThat(repository.updateCountCalls).containsExactly(UpdateCountCall(1, 7, 3, UpdateType.DECREMENT))
    }

    @Test
    fun `decrement floors at zero`() = runTest(mainDispatcherExtension.dispatcher) {
        val viewModel = createViewModel()
        primeState(viewModel, japaId = 1, japa = makeJapa(currentCount = 2))

        viewModel.actionEvent.test {
            viewModel.decrementCount(5)
            awaitItem()
        }
        assertThat(repository.updateCountCalls).containsExactly(UpdateCountCall(1, 0, 5, UpdateType.DECREMENT))
    }

    @Test
    fun `decrement emits update success`() = runTest(mainDispatcherExtension.dispatcher) {
        val viewModel = createViewModel()
        primeState(viewModel, japaId = 1, japa = makeJapa(currentCount = 10))

        viewModel.actionEvent.test {
            viewModel.decrementCount(3)
            assertThat(awaitItem()).isEqualTo(JapaDetailsAction.UpdateSuccess)
        }
    }

    @Test
    fun `decrement repository error emits failure`() = runTest(mainDispatcherExtension.dispatcher) {
        repository.updateCountError = RuntimeException("decrement failed")
        val viewModel = createViewModel()
        primeState(viewModel, japaId = 1, japa = makeJapa(currentCount = 10))

        viewModel.actionEvent.test {
            viewModel.decrementCount(3)
            val result = awaitItem()
            assertThat(result).isInstanceOf(JapaDetailsAction.Failure::class)
        }
    }

    @Test
    fun `markComplete calls repository`() = runTest(mainDispatcherExtension.dispatcher) {
        val viewModel = createViewModel()

        viewModel.actionEvent.test {
            viewModel.markComplete()
            awaitItem()
        }
        assertThat(repository.markCompleteCalls).containsExactly(1)
    }

    @Test
    fun `markComplete emits completion success`() = runTest(mainDispatcherExtension.dispatcher) {
        val viewModel = createViewModel()

        viewModel.actionEvent.test {
            viewModel.markComplete()
            assertThat(awaitItem()).isEqualTo(JapaDetailsAction.CompletionSuccess)
        }
    }

    @Test
    fun `markComplete repository error emits failure`() = runTest(mainDispatcherExtension.dispatcher) {
        repository.markCompleteError = RuntimeException("complete failed")
        val viewModel = createViewModel()

        viewModel.actionEvent.test {
            viewModel.markComplete()
            val result = awaitItem()
            assertThat(result).isInstanceOf(JapaDetailsAction.Failure::class)
        }
    }

    @Test
    fun `resetCounter calls repository`() = runTest(mainDispatcherExtension.dispatcher) {
        val viewModel = createViewModel()

        viewModel.actionEvent.test {
            viewModel.resetCounter()
            awaitItem()
        }
        assertThat(repository.resetCounterCalls).containsExactly(1)
    }

    @Test
    fun `resetCounter emits update success`() = runTest(mainDispatcherExtension.dispatcher) {
        val viewModel = createViewModel()

        viewModel.actionEvent.test {
            viewModel.resetCounter()
            assertThat(awaitItem()).isEqualTo(JapaDetailsAction.UpdateSuccess)
        }
    }

    @Test
    fun `resetCounter repository error emits failure`() = runTest(mainDispatcherExtension.dispatcher) {
        repository.resetCounterError = RuntimeException("reset failed")
        val viewModel = createViewModel()

        viewModel.actionEvent.test {
            viewModel.resetCounter()
            val result = awaitItem()
            assertThat(result).isInstanceOf(JapaDetailsAction.Failure::class)
        }
    }

    @Test
    fun `deleteJapa calls repository`() = runTest(mainDispatcherExtension.dispatcher) {
        val viewModel = createViewModel()

        viewModel.actionEvent.test {
            viewModel.deleteJapa()
            awaitItem()
        }
        assertThat(repository.deleteJapaCalls).containsExactly(1)
    }

    @Test
    fun `deleteJapa emits delete success`() = runTest(mainDispatcherExtension.dispatcher) {
        val viewModel = createViewModel()

        viewModel.actionEvent.test {
            viewModel.deleteJapa()
            assertThat(awaitItem()).isEqualTo(JapaDetailsAction.DeleteSuccess)
        }
    }

    @Test
    fun `deleteJapa repository error emits failure`() = runTest(mainDispatcherExtension.dispatcher) {
        repository.deleteJapaError = RuntimeException("delete failed")
        val viewModel = createViewModel()

        viewModel.actionEvent.test {
            viewModel.deleteJapa()
            val result = awaitItem()
            assertThat(result).isInstanceOf(JapaDetailsAction.Failure::class)
        }
    }

    @Test
    fun `isLoading resets to false after any action`() = runTest(mainDispatcherExtension.dispatcher) {
        val viewModel = createViewModel()
        assertThat(viewModel.isLoading.value).isFalse()

        viewModel.actionEvent.test {
            viewModel.markComplete()
            awaitItem()
        }

        assertThat(viewModel.isLoading.value).isFalse()
    }

    @Test
    fun `isLoading resets to false even on repository error`() = runTest(mainDispatcherExtension.dispatcher) {
        repository.markCompleteError = RuntimeException("complete failed")
        val viewModel = createViewModel()
        assertThat(viewModel.isLoading.value).isFalse()

        viewModel.actionEvent.test {
            viewModel.markComplete()
            awaitItem()
        }

        assertThat(viewModel.isLoading.value).isFalse()
    }
}
