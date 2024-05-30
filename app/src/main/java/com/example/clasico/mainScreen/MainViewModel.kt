package com.example.clasico.mainScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.clasico.model.MainRepository
import com.example.clasico.model.local.student.Student
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainViewModel(
    private val mainRepository: MainRepository
): ViewModel()  {

    private lateinit var netDisposable :Disposable
    private val errorData = MutableLiveData<String>()
    init {
        mainRepository
            .refreshData()
            .subscribeOn(Schedulers.io())
            .subscribe( object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                    netDisposable = d
                }

                override fun onComplete() { }

                override fun onError(e: Throwable) {
                    errorData.postValue( e.message ?: "unknown error!" )
                }
            })
    }

    fun getAllData() : LiveData<List<Student>> {
        return mainRepository.getAllStudent()
    }
    fun getErrorData() :LiveData<String> {
        return errorData
    }

//    val progressBarSubject = BehaviorSubject.create<Boolean>()
//
//    fun getAllStudents() : Single<List<Student>> {
//        progressBarSubject.onNext(true)
//
//        return mainRepository
//            .getAllStudents()
//            .delay(2 , TimeUnit.SECONDS)  //for show progress bar ui to user
//            .doFinally {
//                progressBarSubject.onNext(false)
//            }
//    }

    fun removeStudent(studentName :String) : Completable {
        return mainRepository.removeStudent(studentName)
    }

    override fun onCleared() {
        netDisposable.dispose()
        super.onCleared()
    }

}