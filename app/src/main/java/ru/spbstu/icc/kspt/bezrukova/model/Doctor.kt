package ru.spbstu.icc.kspt.bezrukova.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Doctor(
    val doctorId: Int,
    val name: String,
    val category: String,
    val birthDay: String,
    val phone: String,
    val login: String,
    val password: String,
    val role: Role,
    val specialization: Specialization,
    val id: Int
) : Parcelable