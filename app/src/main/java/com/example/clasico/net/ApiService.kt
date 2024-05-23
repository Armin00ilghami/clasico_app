package com.example.clasico.net

import com.example.clasico.recycler.Student
import retrofit2.Call
import retrofit2.http.GET


interface ApiService {

    @GET("/student")
    fun getAllStudents(): Call<List<Student>>



}