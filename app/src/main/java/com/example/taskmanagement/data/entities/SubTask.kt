package com.example.taskmanagement.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SubTask(

    @PrimaryKey(autoGenerate = true)
    val subTaskId: Int,
    val subTaskTitle: String,
    var isCompleted: Boolean = false,

    val taskId: Int

)
