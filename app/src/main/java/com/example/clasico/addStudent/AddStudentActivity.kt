package com.example.clasico.addStudent

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.clasico.R
import com.example.clasico.databinding.ActivityMain2Binding
import com.example.clasico.model.MainRepository
import com.example.clasico.model.local.MyDatabase
import com.example.clasico.model.local.student.Student
import com.example.clasico.util.AddStudentViewModelFactory
import com.example.clasico.util.ApiServiceSingleton
import com.example.clasico.util.asyncRequest
import com.example.clasico.util.showToast
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

class AddStudentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding
    private lateinit var addStudentViewModel: AddStudentViewModel
    private val compositeDisposable = CompositeDisposable()
    private var isInserting = true

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        enableEdgeToEdge()  //new structure
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMain2)

        addStudentViewModel = ViewModelProvider(
            this ,
            AddStudentViewModelFactory(
                MainRepository(
                    ApiServiceSingleton.apiService!! ,
                    MyDatabase.getDatabase(applicationContext).studentDao
                )
            )
        ).get(AddStudentViewModel::class.java)

        //new structure ->
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main2)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.edtFirstName.requestFocus()


        val testMode = intent.getParcelableExtra<Student>("student")
        isInserting = (testMode == null)

        if (!isInserting) {
            logicUpdateStudent()
        }


        binding.btnDone.setOnClickListener {

            if (isInserting) {
                addNewStudent()
            } else {
                updateStudent()
            }

        }

    }
    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }


    @SuppressLint("SetTextI18n")
    private fun logicUpdateStudent() {

        binding.btnDone.text = "update"

        val dataFromIntent = intent.getParcelableExtra<Student>("student")!!
        binding.edtScore.setText(dataFromIntent.score.toString())
        binding.edtCourse.setText(dataFromIntent.course)

        val splitedName = dataFromIntent.name.split(" ")
        binding.edtFirstName.setText(splitedName[0])
        binding.edtLastName.setText(splitedName[(splitedName.size - 1)])
    }
    private fun updateStudent() {

        val firstName = binding.edtFirstName.text.toString()
        val lastName = binding.edtLastName.text.toString()
        val score = binding.edtScore.text.toString()
        val course = binding.edtCourse.text.toString()

        if (
            firstName.isNotEmpty() &&
            lastName.isNotEmpty() &&
            course.isNotEmpty() &&
            score.isNotEmpty()
        ) {

            addStudentViewModel
                .updateStudent(
                    Student("$firstName $lastName", course, score.toInt())
                )
                .asyncRequest()
                .subscribe(object : CompletableObserver {
                    override fun onSubscribe(d: Disposable) {
                        compositeDisposable.add(d)
                    }

                    override fun onComplete() {
                        showToast("student updated :)")
                        onBackPressed()
                    }

                    override fun onError(e: Throwable) {
                        showToast(("error -> " + e.message) ?: "null")
                    }
                })

        } else {
            showToast("لطفا اطلاعات را کامل وارد کنید")
        }

    }
    private fun addNewStudent() {

        val firstName = binding.edtFirstName.text.toString()
        val lastName = binding.edtLastName.text.toString()
        val score = binding.edtScore.text.toString()
        val course = binding.edtCourse.text.toString()

        if (
            firstName.isNotEmpty() &&
            lastName.isNotEmpty() &&
            course.isNotEmpty() &&
            score.isNotEmpty()
            )
        {

            addStudentViewModel
                .insertNewStudent(
                    Student("$firstName $lastName", course, score.toInt())
                )
                .asyncRequest()
                .subscribe(object : CompletableObserver {
                    override fun onSubscribe(d: Disposable) {
                        compositeDisposable.add(d)
                    }

                    override fun onComplete() {
                        showToast("student inserted :)")
                        onBackPressed()
                    }

                    override fun onError(e: Throwable) {
                        showToast(("error -> " + e.message) ?: "null")
                    }

                })


        } else {
            showToast("لطفا اطلاعات را کامل وارد کنید")
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return true
    }

}