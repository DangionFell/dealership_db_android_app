package com.example.dbcardealership.client

data class Client(
    val id: Int,
    val name: String,
    val phone: String,
    val password: String
)

data class ClientRegisterModel(
    val name: String,
    val phone: String,
    val password: String
)
