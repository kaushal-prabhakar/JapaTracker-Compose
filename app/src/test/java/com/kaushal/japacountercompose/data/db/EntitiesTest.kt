package com.kaushal.japacountercompose.data.db

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.kaushal.japacountercompose.domain.JapaStatus
import com.kaushal.japacountercompose.domain.UpdateType
import java.time.LocalDateTime
import java.util.Locale
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class EntitiesTest {

    @BeforeEach
    fun setUp() {
        Locale.setDefault(Locale.US)
    }

    private fun makeEntity(
        target: Int? = 108,
        lastUpdatedTime: LocalDateTime = LocalDateTime.of(2024, 3, 15, 14, 30)
    ) = JapaInfoDBEntity(
        rowId = 7,
        name = "Hari Krishna",
        target = target,
        lastUpdatedTime = lastUpdatedTime,
        currentCount = 42,
        updatedValue = 1,
        updatedType = UpdateType.INCREMENT,
        status = JapaStatus.ACTIVE
    )

    @Test
    fun `maps all fields correctly`() {
        val result = makeEntity().toJapaInfoEntities()

        assertThat(result.id).isEqualTo(7)
        assertThat(result.name).isEqualTo("Hari Krishna")
        assertThat(result.target).isEqualTo(108)
        assertThat(result.status).isEqualTo(JapaStatus.ACTIVE)
        assertThat(result.currentCount).isEqualTo(42)
        assertThat(result.lastUpdatedValue).isEqualTo(1)
        assertThat(result.lastUpdatedType).isEqualTo(UpdateType.INCREMENT)
    }

    @Test
    fun `formats date correctly`() {
        val result = makeEntity(lastUpdatedTime = LocalDateTime.of(2024, 3, 15, 14, 30)).toJapaInfoEntities()
        assertThat(result.lastUpdatedTime).isEqualTo("15 Mar 2024 • 02:30 PM")
    }

    @Test
    fun `null target preserved as null`() {
        val result = makeEntity(target = null).toJapaInfoEntities()
        assertThat(result.target).isNull()
    }

    @Test
    fun `midnight formats as 12 AM`() {
        val result = makeEntity(lastUpdatedTime = LocalDateTime.of(2024, 1, 1, 0, 0)).toJapaInfoEntities()
        assertThat(result.lastUpdatedTime).isEqualTo("01 Jan 2024 • 12:00 AM")
    }
}
