package com.example.curso2

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Tareas::class],version=1, exportSchema = false)
abstract class TareasDatabase:RoomDatabase() {
    abstract fun tareasDao():TareasDAO

    companion object{
        @Volatile
        private var Instance:TareasDatabase?=null

        fun getDatabase(context: Context):TareasDatabase{
            return Instance?: synchronized(this){
                Room.databaseBuilder(context,TareasDatabase::class.java,"tareas.db").allowMainThreadQueries().build()
                    .also { Instance = it }
            }
        }
    }
}