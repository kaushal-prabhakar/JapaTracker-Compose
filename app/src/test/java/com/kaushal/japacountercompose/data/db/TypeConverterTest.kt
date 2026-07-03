package com.kaushal.japacountercompose.data.db

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import java.time.LocalDateTime
import java.time.format.DateTimeParseException
import org.junit.jupiter.api.Test

class TypeConverterTest {

    @Test
    fun `fromLocalDateTime produces ISO-8601`() {
        val ldt = LocalDateTime.of(2024, 1, 15, 10, 30, 0)
        assertThat(TypeConverter.fromLocalDateTime(ldt)).isEqualTo("2024-01-15T10:30:00")
    }

    @Test
    fun `toLocalDateTime parses ISO-8601`() {
        val result = TypeConverter.toLocalDateTime("2024-01-15T10:30:00")
        assertThat(result).isEqualTo(LocalDateTime.of(2024, 1, 15, 10, 30, 0))
    }

    @Test
    fun `round trip is identity`() {
        val ldt = LocalDateTime.of(2024, 6, 20, 23, 59, 59)
        val result = TypeConverter.toLocalDateTime(TypeConverter.fromLocalDateTime(ldt))
        assertThat(result).isEqualTo(ldt)
    }

    @Test
    fun `toLocalDateTime invalid string throws`() {
        assertFailure { TypeConverter.toLocalDateTime("not-a-date") }
            .isInstanceOf(DateTimeParseException::class)
    }
}
