package com.example.clasico.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.clasico.addStudent.AddStudentViewModel
import com.example.clasico.mainScreen.MainViewModel
import com.example.clasico.model.MainRepository


class MainViewModelFactory(private val mainRepository: MainRepository) :ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(mainRepository) as T
    }

}

class AddStudentViewModelFactory(private val mainRepository: MainRepository) :ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddStudentViewModel(mainRepository) as T
    }

}