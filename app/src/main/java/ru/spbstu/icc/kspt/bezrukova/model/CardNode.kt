package ru.spbstu.icc.kspt.bezrukova.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CardNode(
    val id: Int,
    val date: String,
    val complaints: String,
    val treatment: String,
    val test: String,
    val doctor: Doctor
) : Parcelable