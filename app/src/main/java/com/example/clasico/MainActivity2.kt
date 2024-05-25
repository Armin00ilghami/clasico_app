package com.example.clasico

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.google.gson.JsonObject
import com.example.clasico.databinding.ActivityMain2Binding
import com.example.clasico.net.ApiService
import com.example.clasico.recycler.Student
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity2 : AppCompatActivity() {

    lateinit var binding: ActivityMain2Binding
    lateinit var apiService: ApiService
    var isInserting = true

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        enableEdgeToEdge()  //new structure
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMain2)
        //new structure ->
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main2)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.edtFirstName.requestFocus()

        val retrofit = Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)


        val testMode = intent.getParcelableExtra<Student>("student")
        isInserting = (testMode == null)

        if (!isInserting) {

            binding.btnDone.text = "update"

            val dataFromIntent = intent.getParcelableExtra<Student>("student")!!
            binding.edtScore.setText(dataFromIntent.score.toString())
            binding.edtCourse.setText(dataFromIntent.course)

            val splitedName = dataFromIntent.name.split(" ")
            binding.edtFirstName.setText(splitedName[0])
            binding.edtLastName.setText(splitedName[(splitedName.size - 1)])

        }


        binding.btnDone.setOnClickListener {

            if (isInserting) {
                addNewStudent()
            } else {
                updateStudent()
            }

        }

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

            val jsonObject = JsonObject()
            jsonObject.addProperty("name", "$firstName $lastName")
            jsonObject.addProperty("course", course)
            jsonObject.addProperty("score", score.toInt())


            apiService
                .updateStudent("$firstName $lastName", jsonObject)
                .enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {

                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                    }
                })

            Toast.makeText(this, "update finished!", Toast.LENGTH_SHORT).show()
            onBackPressed()


        } else {
            Toast.makeText(this, "لطفا اطلاعات را کامل وارد کنید", Toast.LENGTH_SHORT).show()
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

            val jsonObject = JsonObject()
            jsonObject.addProperty("name", "$firstName $lastName")
            jsonObject.addProperty("course", course)
            jsonObject.addProperty("score", score.toInt())

            apiService.insertStudent(jsonObject).enqueue(object : Callback<String>{
                override fun onResponse(call : Call<String>, response: Response<String>) { }

                override fun onFailure(call : Call<String>, t : Throwable) {
                    Log.v("apiLog" , t.message!!)
                }
            })

            Toast.makeText(this@MainActivity2, "add student done!", Toast.LENGTH_SHORT).show()
            onBackPressed()

        } else {
            Toast.makeText(this, "لطفا اطلاعات را کامل وارد کنید", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return true
    }

}