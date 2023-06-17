package com.example.dbcardealership.manager

data class Manager(
    val id: Int,
    val name: String,
    val phone: String,
    val password: String,
    val showroomId: Int
)

data class ManagerRegisterModel(
    val name: String,
    val phone: String,
    val password: String,
    val showroomId: Int
)
