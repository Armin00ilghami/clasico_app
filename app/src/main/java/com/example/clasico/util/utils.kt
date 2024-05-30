package com.example.clasico.util

import com.example.clasico.model.local.student.Student
import com.google.gson.JsonObject

fun studentToJsonObject(student: Student) :JsonObject {

    val jsonObject = JsonObject()
    jsonObject.addProperty("name", student.name)
    jsonObject.addProperty("course", student.course)
    jsonObject.addProperty("score", student.score)
    return jsonObject

}