package com.example.taskmanager.cloudDb

import com.example.taskmanager.data.Task


interface ICloudDb {

    //clear existing items in cloudDb, then upload all items from roomDb
    fun uploadItems()

    //clear existing items in cloudDb, then upload selected items from roomDb
    fun uploadItems(items: List<Task>)

    //clear existing items in roomDb, then download all items from cloudDb
    fun downloadAllItems()

    fun clearDb()

}