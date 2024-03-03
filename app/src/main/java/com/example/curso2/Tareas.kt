package com.example.curso2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tareas")
data class Tareas(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val descrip:String,
    var checked:Boolean)
