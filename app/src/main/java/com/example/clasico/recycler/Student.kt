package com.example.clasico.recycler

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Student(

    val name: String,
    val course: String,
    val score: Int

):Parcelable
