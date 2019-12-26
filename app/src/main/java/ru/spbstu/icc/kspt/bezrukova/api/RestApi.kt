package ru.spbstu.icc.kspt.bezrukova.api

import com.google.gson.GsonBuilder
import io.reactivex.Completable
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import ru.spbstu.icc.kspt.bezrukova.model.*

interface RestApi {

    @POST("/login")
    fun requestUserLogin(@Body login: RequestBody): Observable<User>

    @POST("/login")
    fun requestAdminLogin(@Body login: RequestBody): Observable<Admin>

    @POST("/login")
    fun requestDoctorLogin(@Body login: RequestBody): Observable<Doctor>

    @POST("/requests")
    fun getRequests(@Body login: RequestBody): Observable<List<Request>>

    @POST("/records")
    fun getRecords(@Body login: RequestBody): Observable<List<Record>>

    @POST("/doc_records")
    fun getRecordsForDoctor(@Body login: RequestBody): Observable<List<Record>>

    @GET("/doc_specialisations")
    fun getAllSpecialisations(): Observable<List<DoctorCategory>>

    @POST("/docs_with_specialisation")
    fun getDoctorsWithSpecialisation(@Body request: RequestBody): Observable<List<Doctor>>

    @POST("/doc_records_free_time")
    fun getFreeTimeForDoctor(@Body request: RequestBody): Observable<List<String>>

    @POST("/create_request")
    fun createRequest(@Body request: RequestBody): Observable<Request>

    @POST("/remove_record")
    fun removeRecord(@Body request: RequestBody): Observable<Request>

    @POST("/update_request")
    fun updateRequest(@Body request: RequestBody): Observable<Request>

    @POST("/card")
    fun getUserCard(@Body request: RequestBody): Observable<UserCard>

    @POST("/card_notes")
    fun getUserCardNotes(@Body request: RequestBody): Observable<List<CardNode>>

    @POST("/doctor_requests")
    fun getDoctorRequests(@Body login: RequestBody): Observable<List<Request>>

    @GET("/all_requests")
    fun getAllRequests(): Observable<List<Request>>

    companion object {
        fun get(): RestApi {
            val gson = GsonBuilder()
                .setLenient()
                .create()

            val okHttpClient = OkHttpClient.Builder().addInterceptor { chain ->
                chain.run {
                    proceed(
                        request().newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Accept", "application/json")
                            .build()
                    )
                }
            }.build()

            val retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
//                .baseUrl("http://192.168.42.202:80/")
//                .baseUrl("http://192.168.43.16:80/")
                .baseUrl("http://192.168.0.105:80/")
//                .baseUrl("http://10.0.2.2:80/")
                .build()

            return retrofit.create(RestApi::class.java)
        }
    }
}