package com.example.todoapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User (
    @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
    @ColumnInfo val name: String,
    @ColumnInfo val email: String,
    @ColumnInfo val password: String
)