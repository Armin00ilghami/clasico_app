package com.example.clasico.model

import androidx.lifecycle.LiveData
import com.example.clasico.model.api.ApiService
import com.example.clasico.model.local.student.Student
import com.example.clasico.model.local.student.StudentDao
import com.example.clasico.util.studentToJsonObject
import io.reactivex.rxjava3.core.Completable


class MainRepository (
    private val apiService: ApiService ,
    private val studentDao: StudentDao
){

    fun getAllStudent(): LiveData<List<Student>> {
        return studentDao.getAllData()
    }

    // for caching
    fun refreshData(): Completable {
        return apiService
            .getAllStudents()
            .doOnSuccess {
                studentDao.insertAll(it)
            }
            .ignoreElement()
    }


//    fun getAllStudents(): Single<List<Student>> {
//        return apiService.getAllStudents()
//    }

    fun insertStudent(student: Student): Completable {
        return apiService
            .insertStudent(studentToJsonObject(student))
            .doOnComplete {
                studentDao.insertOrUpdate(student)
            }
    }

    fun updateStudent(student: Student): Completable {
        return apiService
            .updateStudent(student.name, studentToJsonObject(student))
            .doOnComplete {
                studentDao.insertOrUpdate(student)
            }
    }

    fun removeStudent(studentName: String): Completable {
        return apiService
            .deleteStudent(studentName)
            .doOnComplete {
                studentDao.remove(studentName)
            }
    }


}