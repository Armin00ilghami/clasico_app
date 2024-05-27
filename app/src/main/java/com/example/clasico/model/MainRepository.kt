package com.example.clasico.model

import com.example.clasico.util.BASE_URL
import com.example.clasico.util.studentToJsonObject
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainRepository {
    private val apiService: ApiService

    init {
        val retrofit = Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory( RxJava3CallAdapterFactory.create())
            .build()


        apiService = retrofit.create(ApiService::class.java)

    }

    fun getAllStudents(): Single<List<Student>> {
        return apiService.getAllStudents()
    }
    fun insertStudent(student: Student): Completable {
        return apiService.insertStudent(studentToJsonObject(student))
    }

    fun updateStudent(student: Student): Completable {
        return apiService.updateStudent(student.name, studentToJsonObject(student))
    }

    fun removeStudent(studentName: String): Completable {
        return apiService.deleteStudent(studentName)
    }


}