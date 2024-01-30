package com.example.taskmanager.cloudDb.fireStore

import android.provider.Settings
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

const val COLLECTION_NAME_PREFIX = "COLLECTION_NAME_PREFIX"
const val COLLECTION_NAME = "COLLECTION_NAME"

@Singleton
class FirestoreDB @Inject constructor(
    val repository: TaskRepository
) : ICloudDb {

    val TAG = this.javaClass.simpleName
    val database = Firebase.firestore
    val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())


    private val collectionName = COLLECTION_NAME//_PREFIX + Settings.Secure.ANDROID_ID


    fun delAndUpload(items: List<Task>) {
        scope.launch {
            deleteAllItems()
            uploadAllItems(items)

        }
    }

    override fun uploadAllItems(items: List<Task>) {
        try {

            items.forEach { item ->
                database
                    .collection(collectionName)
                    .add(item.toHashMap())
                    .addOnSuccessListener {
                        Log.e(TAG, "${it}")
                    }
                    .addOnFailureListener {
                        Log.e(TAG, "${it.message}")
                    }
                    .addOnCompleteListener {
                        Log.e(TAG, "${it}")
                    }
                    .addOnCanceledListener {
                        Log.e(TAG, "Upload Canceled")
                    }
            }
        } catch (ex: Exception) {
            Log.e(TAG, "${ex.message}")
        }
    }

    override fun downloadAllItems(): List<Task> {
        val result = mutableListOf<Task>()
        database.collection(collectionName)
            .get()
            .addOnSuccessListener { snapShot ->
                snapShot.forEach { document ->
                    result.add(
                        Task.from(document.data)
                    )
                }
            }
        return result
    }

    override fun downloadAllItemsAndSaveInDb() {
        try {
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
        catch (ex:Exception){
            Log.e(TAG,"${ex.message}")
        }
    }

    override suspend fun deleteAllItems() {
        try {
            // val id=database.collection(collectionName).id
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