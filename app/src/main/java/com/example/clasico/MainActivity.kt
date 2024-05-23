package com.example.clasico

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.annotation.SuppressLint
import android.content.Intent
import android.os.BaseBundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.clasico.databinding.ActivityMainBinding
import com.example.clasico.net.ApiService
import com.example.clasico.recycler.Student
import com.example.clasico.recycler.StudentAdapter
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Headers

const val BASE_URL = "http://192.168.1.34:8080"

class MainActivity : AppCompatActivity(),StudentAdapter.StudentEvent {

    lateinit var binding: ActivityMainBinding
    lateinit var myAdapter: StudentAdapter
    lateinit var apiService: ApiService

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge() //new structure
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMain)
        //new structure ->
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val retrofit = Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)

        binding.btnAddStudent.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }


    }

    override fun onResume() {
        super.onResume()

        getDataFromApi()
    }

    fun getDataFromApi() {

        apiService.getAllStudents().enqueue(object : Callback<List<Student>> {
            override fun onResponse(call : Call<List<Student>>, response: Response<List<Student>>) {

                val dataFromServer = response.body()!!
                setDataToRecycler(dataFromServer)

            }

            override fun onFailure(p0: Call<List<Student>>, t: Throwable) {
                Log.v("testApi", t.message!!)
            }

        })

    }

    fun setDataToRecycler(data: List<Student>) {
        val myData = ArrayList(data)
        myAdapter = StudentAdapter(myData, this)
        binding.recyclerMain.adapter = myAdapter
        binding.recyclerMain.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    override fun onItemClicked(student: Student, position: Int) {
        updateDataInServer(student, position)
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

    private fun deleteDataFromServer(student: Student, position: Int) {

    }

    private fun updateDataInServer(student: Student, position: Int) {

    }


}