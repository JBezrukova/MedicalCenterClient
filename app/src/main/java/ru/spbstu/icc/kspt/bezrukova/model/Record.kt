package ru.spbstu.icc.kspt.bezrukova.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Record(val id: Int,
                  val date: String,
                  val time: String,
                  val user: User,
                  val doctor: Doctor) : Parcelable