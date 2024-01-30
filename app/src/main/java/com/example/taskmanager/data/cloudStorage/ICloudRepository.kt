package com.example.taskmanager.data.cloudStorage

import com.example.taskmanager.data.Task

interface ICloudRepository {

    fun uploadTasks(tasks: List<Task>)

    fun downloadTasks():List<Task>

}