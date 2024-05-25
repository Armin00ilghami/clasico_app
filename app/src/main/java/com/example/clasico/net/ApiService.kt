package com.example.clasico.net

import com.example.clasico.recycler.Student
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface ApiService {

    @GET("/student")
    fun getAllStudents(): Call<List<Student>>

    @POST("/student")
    fun insertStudent(@Body body: JsonObject): Call<String>

    @PUT("/student/updating{name}")
    fun updateStudent(@Path("name") name:String, @Body body :JsonObject ) :Call<String>

    @DELETE("/student/deleting{name}")
    fun deleteStudent( @Path("name") name:String ) :Call<String>

}