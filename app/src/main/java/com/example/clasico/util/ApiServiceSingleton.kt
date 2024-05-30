package com.example.clasico.util

import com.example.clasico.model.api.ApiService
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiServiceSingleton {
    var apiService: ApiService? = null
        get() {
            if (field == null) {
                val retrofit = Retrofit
                    .Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build()

                field = retrofit.create(ApiService::class.java)
            }
            return field
        }
}