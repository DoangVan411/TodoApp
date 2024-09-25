package com.example.todoapp.database

import androidx.room.TypeConverter
import java.sql.Timestamp

class TimestampConverter {

    @TypeConverter
    fun fromTimestamp(value: Long?): Timestamp? {
        return value?.let { Timestamp(it) }
    }

    @TypeConverter
    fun dateToTimestamp(timestamp: Timestamp?): Long? {
        return timestamp?.time
    }
}