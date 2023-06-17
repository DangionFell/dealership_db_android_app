package com.example.dbcardealership.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: Int,
    val name: String,
    val phone: String,
    val user_state: String
)

data class UserLogin(
    val phone: String,
    val password: String
)

