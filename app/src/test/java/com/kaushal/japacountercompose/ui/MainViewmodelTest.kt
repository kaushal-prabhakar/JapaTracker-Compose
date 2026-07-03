package com.kaushal.japacountercompose.ui

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.kaushal.japacountercompose.MainDispatcherExtension
import com.kaushal.japacountercompose.domain.JapaInfoEntities
import com.kaushal.japacountercompose.domain.JapaStatus
import com.kaushal.japacountercompose.domain.UpdateType
import com.kaushal.japacountercompose.fakes.FakeMainRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class MainViewmodelTest {

    @JvmField
    @RegisterExtension
    val mainDispatcherExtension = MainDispatcherExtension()

    private val repository = FakeMainRepository()

    private fun makeJapa() = JapaInfoEntities(
        id = 1,
        name = "Hari Krishna",
        target = 108,
        status = JapaStatus.ACTIVE,
        currentCount = 10,
        lastUpdatedValue = 1,
        lastUpdatedType = UpdateType.INCREMENT,
        lastUpdatedTime = "15 Mar 2024 • 02:30 PM"
    )

    @Test
    fun `before repository responds, state is loading`() = runTest(mainDispatcherExtension.dispatcher) {
        val viewModel = MainViewmodel(repository)

        viewModel.startDestination.test {
            assertThat(awaitItem()).isEqualTo(StartDestinationState.Loading)
        }
    }

    @Test
    fun `empty japa list, start destination is welcome`() = runTest(mainDispatcherExtension.dispatcher) {
        val viewModel = MainViewmodel(repository)

        viewModel.startDestination.test {
            assertThat(awaitItem()).isEqualTo(StartDestinationState.Loading)
            repository.japaListFlow.value = emptyList()
            assertThat(awaitItem()).isEqualTo(StartDestinationState.Ready(JapaAppScreens.welcome.name))
        }
    }

    @Test
    fun `non-empty japa list, start destination is japaList`() = runTest(mainDispatcherExtension.dispatcher) {
        val viewModel = MainViewmodel(repository)

        viewModel.startDestination.test {
            assertThat(awaitItem()).isEqualTo(StartDestinationState.Loading)
            repository.japaListFlow.value = listOf(makeJapa())
            assertThat(awaitItem()).isEqualTo(StartDestinationState.Ready(JapaAppScreens.japaList.name))
        }
    }
}
