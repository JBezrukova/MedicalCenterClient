package ru.spbstu.icc.kspt.bezrukova.model

data class UserCard(
    val user: User,
    val passport: String,
    val oms: String,
    val chronicDesease: String,
    val notes: String
)