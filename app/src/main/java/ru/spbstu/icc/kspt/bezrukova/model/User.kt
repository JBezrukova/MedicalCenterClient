package ru.spbstu.icc.kspt.bezrukova.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User (val userId: Int,
                 val login: String,
                 val password: String,
                 val userName: String,
                 val adres: String,
                 val birthDay: String,
                 val phone: String,
                 val sex: String,
                 val role: Role) : Parcelable