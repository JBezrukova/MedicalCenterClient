package ru.spbstu.icc.kspt.bezrukova.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

enum class Role : Serializable {
    @SerializedName("user")
    USER,
    @SerializedName("admin")
    ADMIN,
    @SerializedName("doctor")
    DOCTOR
}