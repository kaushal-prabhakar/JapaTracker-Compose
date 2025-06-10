package com.kaushal.japacountercompose.data.db

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object TypeConverter {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    // store as ISO-8601 string
    @TypeConverter
    @JvmStatic
    fun fromLocalDateTime(value: LocalDateTime): String {
        return value.format(formatter)
    }

    @TypeConverter
    @JvmStatic
    fun toLocalDateTime(value: String): LocalDateTime {
        return LocalDateTime.parse(value, formatter)
    }
}