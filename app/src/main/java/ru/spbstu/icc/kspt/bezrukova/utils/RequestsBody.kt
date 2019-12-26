package ru.spbstu.icc.kspt.bezrukova.utils

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

object RequestsBody {

    fun getLoginPassword(login: String, password: String): RequestBody {
        val request = """ { "login" : "$login", "password" : "$password" } """
        return getRequestBody(request)
    }

    fun getLogin(login: String): RequestBody {
        val request = """ { "login" : "$login" } """
        return getRequestBody(request)
    }

    fun getId(id: Int) : RequestBody {
        val request = """ { "id" : $id } """
        return getRequestBody(request)
    }

    fun getFreeTimeForDoctor(id: Int, day: String) : RequestBody {
        val request = """ { "id" : $id, "day" : "$day" } """
        return getRequestBody(request)
    }

    fun createRequest(date: String, time: String, userId: Int, doctorId: Int) : RequestBody {
        val request = """ { "date" : "$date", "time" : "$time", "user" : {"userId" : $userId}, "doctor" : {"id" : $doctorId}, "reason": "create" } """
        return getRequestBody(request)
    }

    fun updateRequest(id: Int, date: String, time: String, userId: Int, doctorId: Int, reason: String, approvedByAdmin: Boolean, approvedByDoctor: Boolean) : RequestBody {
        val request = """ { "id": $id, "date" : "$date", "time" : "$time", "user" : {"userId" : $userId}, "doctor" : {"id" : $doctorId}, "reason": "$reason" , "approvedByAdmin": $approvedByAdmin, "approvedByDoctor": $approvedByDoctor } """
        return getRequestBody(request)
    }

    fun getUserId(id: Int) : RequestBody {
        val request = """ { "userId" : $id } """
        return getRequestBody(request)
    }

    private fun getRequestBody(request: String): RequestBody {
        return request.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
    }


}