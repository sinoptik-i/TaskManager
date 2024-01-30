package com.example.taskmanager.cloudDb

import com.example.taskmanager.data.Task


interface ICloudDb {

    fun uploadAllItems(items: List<Task>)

    fun downloadAllItems(): List<Task>

    fun downloadAllItemsAndSaveInDb()

    suspend fun deleteAllItems()

}