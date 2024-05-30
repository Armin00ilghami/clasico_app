package com.example.clasico.mainScreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.clasico.R
import com.example.clasico.addStudent.AddStudentActivity
import com.example.clasico.databinding.ActivityMainBinding
import com.example.clasico.model.MainRepository
import com.example.clasico.model.local.MyDatabase
import com.example.clasico.model.local.student.Student
import com.example.clasico.util.ApiServiceSingleton
import com.example.clasico.util.MainViewModelFactory
import com.example.clasico.util.asyncRequest
import com.example.clasico.util.showToast
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable


class MainActivity : AppCompatActivity(), StudentAdapter.StudentEvent {

    private lateinit var binding: ActivityMainBinding
    private lateinit var myAdapter: StudentAdapter
    private val compositeDisposable = CompositeDisposable()
    private lateinit var mainViewModel: MainViewModel

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge() //new structure
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMain)
        initRecycler()

        mainViewModel = ViewModelProvider(
            this ,
            MainViewModelFactory(
                MainRepository(
                    ApiServiceSingleton.apiService!! ,
                    MyDatabase.getDatabase(applicationContext).studentDao
                )
            )
        ).get(MainViewModel::class.java)

        //new structure ->
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnAddStudent.setOnClickListener {
            val intent = Intent(this, AddStudentActivity::class.java)
            startActivity(intent)
        }

        mainViewModel.getAllData().observe(this) {
            refreshRecyclerData(it)
        }

        mainViewModel.getErrorData().observe(this) {
            Log.e("testLog", it)
        }

    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }


    override fun onItemClicked(student: Student, position: Int) {
        val intent = Intent(this, AddStudentActivity::class.java)
        intent.putExtra("student", student)
        startActivity(intent)
    }
    override fun onItemLongClicked(student: Student, position: Int) {
        val dialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
        dialog.contentText = "Delete this Item?"
        dialog.cancelText = "cancel"
        dialog.confirmText = "confirm"
        dialog.setOnCancelListener {
            dialog.dismiss()
        }

        dialog.setConfirmClickListener {

            deleteDataFromServer(student, position)
            dialog.dismiss()

        }
        dialog.show()
    }


    private fun refreshRecyclerData(newData: List<Student>) {
        myAdapter.refreshData(newData)
    }
    private fun deleteDataFromServer(student: Student, position: Int) {


        mainViewModel
            .removeStudent(student.name)
            .asyncRequest()
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onComplete() {
                    showToast("student removed :)")
                }

                override fun onError(e: Throwable) {
                    showToast(("error -> " + e.message) ?: "null")
                }

            })
    }
    private fun initRecycler() {
        val myData = arrayListOf<Student>()
        myAdapter = StudentAdapter(myData, this)
        binding.recyclerMain.adapter = myAdapter
        binding.recyclerMain.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

}