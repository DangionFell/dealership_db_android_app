package com.example.dbcardealership.сar

data class Car(
    val id: Int,
    val model: String,
    val config: String,
    val yearOfManufacture :String,
    val horsepower :Int,
    val price :Int,
    val color :String,
    val state :String,
    val showroomId :Int
)

data class CarCreateModel(
    val model: String,
    val config: String,
    val yearOfManufacture :String,
    val horsepower :Int,
    val price :Int,
    val color :String,
    val state :String,
    val showroomId :Int
)


