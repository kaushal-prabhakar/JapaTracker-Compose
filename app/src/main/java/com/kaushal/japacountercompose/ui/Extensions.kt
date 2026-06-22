package com.kaushal.japacountercompose.ui

import java.text.NumberFormat
import java.util.Locale

/**
 * Formats a number with comma separators based on the default locale.
 * Example: 10000 -> 10,000
 */
fun Int.formatWithCommas(): String {
    return NumberFormat.getNumberInstance(Locale.getDefault()).format(this)
}

/**
 * Formats a string to Title Case (capitalizes the first letter of each word).
 * Example: "narayana japa" -> "Narayana Japa"
 */
fun String.toTitleCase(): String {
    if (this.isBlank()) return this
    return this.split(" ")
        .filter { it.isNotEmpty() }
        .joinToString(" ") { word ->
            word.lowercase().replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
            }
        }
}
