package com.example.clasico.model.local.student

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.clasico.model.local.student.Student


@Dao
interface StudentDao {


    @Query("SELECT * FROM student")
    fun getAllData(): LiveData<List<Student>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(students: List<Student>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(student: Student)

    @Query("DELETE FROM student WHERE name = :studentName")
    fun remove(studentName: String)

}