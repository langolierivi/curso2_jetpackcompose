package com.example.curso2

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface TareasDAO {
    @Insert
    fun insert(tareas:Tareas)

    @Delete
    fun delete(tareas:Tareas)

    @Update
    fun update(tareas: Tareas)

    @Query("Select * from tareas")
    fun getAll(): List<Tareas>
}