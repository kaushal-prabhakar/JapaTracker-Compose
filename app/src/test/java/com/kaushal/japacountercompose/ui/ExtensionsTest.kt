package com.kaushal.japacountercompose.ui

import assertk.assertThat
import assertk.assertions.isEqualTo
import java.util.Locale
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ExtensionsTest {

    @BeforeEach
    fun setUp() {
        Locale.setDefault(Locale.US)
    }

    @Test
    fun `formatWithCommas zero`() {
        assertThat(0.formatWithCommas()).isEqualTo("0")
    }

    @Test
    fun `formatWithCommas small number, no comma`() {
        assertThat(999.formatWithCommas()).isEqualTo("999")
    }

    @Test
    fun `formatWithCommas thousands`() {
        assertThat(10000.formatWithCommas()).isEqualTo("10,000")
    }

    @Test
    fun `formatWithCommas millions`() {
        assertThat(1_000_000.formatWithCommas()).isEqualTo("1,000,000")
    }

    @Test
    fun `formatWithCommas negative`() {
        assertThat((-5000).formatWithCommas()).isEqualTo("-5,000")
    }

    @Test
    fun `toTitleCase single word`() {
        assertThat("hello".toTitleCase()).isEqualTo("Hello")
    }

    @Test
    fun `toTitleCase multiple words`() {
        assertThat("narayana japa".toTitleCase()).isEqualTo("Narayana Japa")
    }

    @Test
    fun `toTitleCase all uppercase`() {
        assertThat("HELLO WORLD".toTitleCase()).isEqualTo("Hello World")
    }

    @Test
    fun `toTitleCase blank string`() {
        assertThat("".toTitleCase()).isEqualTo("")
    }

    @Test
    fun `toTitleCase extra spaces collapsed`() {
        assertThat("hello  world".toTitleCase()).isEqualTo("Hello World")
    }

    @Test
    fun `toTitleCase single char`() {
        assertThat("a".toTitleCase()).isEqualTo("A")
    }
}
