package com.kaushal.japacountercompose.data.repository

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.kaushal.japacountercompose.data.db.JapaInfoDBEntity
import com.kaushal.japacountercompose.data.db.toJapaInfoEntities
import com.kaushal.japacountercompose.domain.JapaStatus
import com.kaushal.japacountercompose.domain.UpdateType
import com.kaushal.japacountercompose.fakes.FakeRoomDBDao
import java.time.LocalDateTime
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class MainRepositoryImplTest {

    private val dao = FakeRoomDBDao()
    private val repository = MainRepositoryImpl(dao)

    private fun makeDbEntity(
        rowId: Int = 0,
        name: String = "Hari Krishna",
        target: Int? = 108
    ) = JapaInfoDBEntity(
        rowId = rowId,
        name = name,
        target = target,
        lastUpdatedTime = LocalDateTime.of(2024, 3, 15, 14, 30),
        currentCount = 0,
        updatedValue = 0,
        updatedType = UpdateType.INCREMENT,
        status = JapaStatus.NOT_STARTED
    )

    @Test
    fun `getJapaList maps db entities to domain`() = runTest {
        val seeded = dao.seed(makeDbEntity())

        repository.getJapaList().test {
            assertThat(awaitItem()).isEqualTo(listOf(seeded.toJapaInfoEntities()))
        }
    }

    @Test
    fun `getJapaList empty`() = runTest {
        repository.getJapaList().test {
            assertThat(awaitItem()).isEqualTo(emptyList())
        }
    }

    @Test
    fun `getJapaById existing`() = runTest {
        val seeded = dao.seed(makeDbEntity(rowId = 5))

        repository.getJapaById(5).test {
            assertThat(awaitItem()).isEqualTo(seeded.toJapaInfoEntities())
        }
    }

    @Test
    fun `getJapaById missing`() = runTest {
        repository.getJapaById(99).test {
            assertThat(awaitItem()).isNull()
        }
    }

    @Test
    fun `addJapa constructs default entity`() = runTest {
        val rowId = repository.addJapa("Test", 108)
        val stored = dao.entityFor(rowId.toInt())

        assertThat(stored?.name).isEqualTo("Test")
        assertThat(stored?.target).isEqualTo(108)
        assertThat(stored?.currentCount).isEqualTo(0)
        assertThat(stored?.status).isEqualTo(JapaStatus.NOT_STARTED)
        assertThat(stored?.updatedType).isEqualTo(UpdateType.INCREMENT)
    }

    @Test
    fun `addJapa null target`() = runTest {
        val rowId = repository.addJapa("Test", null)
        assertThat(dao.entityFor(rowId.toInt())?.target).isNull()
    }

    @Test
    fun `addJapa returns row id`() = runTest {
        val rowId = repository.addJapa("Test", 108)
        assertThat(rowId).isEqualTo(1L)
    }

    @Test
    fun `updateCount sets active status`() = runTest {
        dao.seed(makeDbEntity(rowId = 1))

        repository.updateCount(id = 1, newCount = 10, updatedValue = 5, updatedType = UpdateType.INCREMENT)

        val stored = dao.entityFor(1)
        assertThat(stored?.status).isEqualTo(JapaStatus.ACTIVE)
        assertThat(stored?.currentCount).isEqualTo(10)
        assertThat(stored?.updatedValue).isEqualTo(5)
        assertThat(stored?.updatedType).isEqualTo(UpdateType.INCREMENT)
    }

    @Test
    fun `markComplete sets completed status`() = runTest {
        dao.seed(makeDbEntity(rowId = 1))

        repository.markComplete(1)

        assertThat(dao.entityFor(1)?.status).isEqualTo(JapaStatus.COMPLETED)
    }

    @Test
    fun `resetCounter sets not-started status`() = runTest {
        dao.seed(makeDbEntity(rowId = 1).copy(currentCount = 50, status = JapaStatus.ACTIVE))

        repository.resetCounter(1)

        val stored = dao.entityFor(1)
        assertThat(stored?.status).isEqualTo(JapaStatus.NOT_STARTED)
        assertThat(stored?.currentCount).isEqualTo(0)
    }

    @Test
    fun `deleteJapa delegates to dao`() = runTest {
        dao.seed(makeDbEntity(rowId = 1))

        repository.deleteJapa(1)

        assertThat(dao.entityFor(1)).isNull()
    }
}
