package com.example.taskmanager.data.cloudStorage.firestore

import com.example.taskmanager.data.Task
import com.example.taskmanager.data.cloudStorage.ICloudRepository

class FireStoreRepository:ICloudRepository {
    override fun uploadTasks(tasks: List<Task>) {
        TODO("Not yet implemented")
    }

    override fun downloadTasks(): List<Task> {
        TODO("Not yet implemented")
    }
}