package ru.spbstu.icc.kspt.bezrukova.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Specialization(val categoryId: Int,
                          val name: String) : Parcelable