package com.example.clasico.model.api

import com.example.clasico.model.local.student.Student
import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface ApiService {

    @GET("/student")
    fun getAllStudents(): Single<List<Student>>

    @POST("/student")
    fun insertStudent(@Body body: JsonObject): Completable

    @PUT("/student/updating{name}")
    fun updateStudent(@Path("name") name:String, @Body body :JsonObject ): Completable

    @DELETE("/student/deleting{name}")
    fun deleteStudent( @Path("name") name:String ): Completable

}