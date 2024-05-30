package com.example.clasico.addStudent

import androidx.lifecycle.ViewModel
import com.example.clasico.model.MainRepository
import com.example.clasico.model.local.student.Student
import io.reactivex.rxjava3.core.Completable

class AddStudentViewModel
    (private val mainRepository: MainRepository) :ViewModel() {

    fun insertNewStudent(student: Student):Completable{
        return mainRepository.insertStudent(student)
    }

    fun updateStudent(student: Student) : Completable {
        return mainRepository.updateStudent(student)
    }

}