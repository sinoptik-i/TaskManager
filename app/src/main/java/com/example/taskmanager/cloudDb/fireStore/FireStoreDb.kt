package com.example.taskmanager.cloudDb.fireStore

import android.util.Log
import com.example.taskmanager.cloudDb.ICloudDb
import com.example.taskmanager.data.Task
import com.example.taskmanager.data.TaskRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

const val COLLECTION_NAME = "COLLECTION_NAME"

@Singleton
class FirestoreDB @Inject constructor(
    private val repository: TaskRepository,
    private val scope: CoroutineScope
) : ICloudDb {

    val TAG = this.javaClass.simpleName
    val database = Firebase.firestore

    private val collectionName = COLLECTION_NAME

    override fun uploadItems() {
        scope.launch {
            clearDbSuspend()
            uploadAllItems()
        }
    }

    override fun uploadItems(items: List<Task>) {
        scope.launch {
            clearDb()
            uploadListOfItems(items)
        }
    }

    private suspend fun uploadAllItems() {
        try {
            repository.getAllTasks().collect { tasks ->
                tasks.forEach { task ->
                    database
                        .collection(collectionName)
                        .add(task.toHashMap())
                }
            }
        } catch (ex: Exception) {
            Log.e(TAG, "${ex.message}")
        }
    }

    //upload items list in cloudDb
    private fun uploadListOfItems(items: List<Task>) {
        try {
            Log.e(TAG, "count: ${items.count()}")
            items.forEach { item ->
                database
                    .collection(collectionName)
                    .add(item.toHashMap())
            }
        } catch (ex: Exception) {
            Log.e(TAG, "${ex.message}")
        }
    }


    private fun downloadAllItemsFromCloudDbAndSaveItInRoom() {
        database.collection(collectionName)
            .get()
            .addOnSuccessListener { snapShot ->
                snapShot.forEach { document ->
                    scope.launch {
                        repository.add(
                            Task.from(document.data)
                        )
                    }
                }
            }
    }


    override fun downloadAllItems() {
        scope.launch {
            repository.clearDb()
            downloadAllItemsFromCloudDbAndSaveItInRoom()
        }
    }

    private suspend fun clearDbSuspend() {
        try {
            database.collection(collectionName)
                .get()
                .await()
                .map {
                    database
                        .collection(collectionName)
                        .document(it.id)
                        .delete()
                        .asDeferred()
                }.joinAll()
        } catch (ex: Exception) {
            Log.e(TAG, "${ex.message}")
        }
    }

    override fun clearDb() {
        scope.launch {
            try {
                database.collection(collectionName)
                    .get()
                    .await()
                    .map {
                        database
                            .collection(collectionName)
                            .document(it.id)
                            .delete()
                            .asDeferred()
                    }.joinAll()
            } catch (ex: Exception) {
                Log.e(TAG, "${ex.message}")
            }
        }
    }
}