package com.example.clasico.mainScreen

import com.example.clasico.model.MainRepository
import com.example.clasico.model.local.student.Student
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

class MainScreenViewModel(private val mainRepository: MainRepository) {

    val progressBarSubject = BehaviorSubject.create<Boolean>()

    fun getAllStudents() : Single<List<Student>> {
        progressBarSubject.onNext(true)

        return mainRepository
            .getAllStudents()
            .delay(2 , TimeUnit.SECONDS)  //for show progress bar ui to user
            .doFinally {
                progressBarSubject.onNext(false)
            }
    }

    fun removeStudent(studentName :String) : Completable {
        return mainRepository.removeStudent(studentName)
    }

}