package ir.dunijet.studentManager.model.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.clasico.model.local.student.Student
import com.example.clasico.model.local.student.StudentDao


@Database(entities = [Student::class], version = 1, exportSchema = false)
abstract class MyDatabase : RoomDatabase() {

    abstract val studentDao: StudentDao

    companion object {

        @Volatile
        private var dataBase: MyDatabase? = null

        fun getDatabase(context: Context): MyDatabase {

            synchronized(this) {
                if (dataBase == null) {
                    dataBase = Room.databaseBuilder(
                        context.applicationContext,
                        MyDatabase::class.java,
                        "myDatabase.db"
                    )
                        .build()
                }

                return dataBase!!
            }

        }
    }

}