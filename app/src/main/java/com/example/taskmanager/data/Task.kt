package com.example.taskmanager.data

import android.annotation.SuppressLint
import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import com.example.taskmanager.DATE_FORMAT
import com.example.taskmanager.TIME_FORMAT
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Calendar

const val TAG = "Task"


@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "title") val title: String ="",// "title",
    @ColumnInfo(name = "description") val description: String ="",// "description",
    @ColumnInfo(name = "completionTime") val completionTime: String = "",
    @ColumnInfo(name = "finished") val finished: Boolean = false,
    @ColumnInfo(name = "attemptCount") val attemptCount: Int = 0,
    @ColumnInfo(name = "reminderTime") val reminderTime: String = "",
) {
    override fun toString(): String = title

    fun toHashMap() = hashMapOf(
        "id" to this.id,
        "title" to this.title,
        "description" to this.description,
        "completionTime" to this.completionTime,
        "finished" to this.finished,
        "attemptCount" to this.attemptCount,
        "reminderTime" to this.reminderTime
    )


    @SuppressLint("SimpleDateFormat")
    fun diffCompletionTimeInMinutes(): Long {
        val formatter = SimpleDateFormat(DATE_FORMAT)
        val nowTime = Calendar.getInstance().timeInMillis
        try {
            val date = formatter.parse(completionTime)
            if (date != null) {
                return (date.time - nowTime) / 60000
            }
        } catch (exception: Exception) {
            Log.e(TAG, "${exception.message}")
        }
        return 0
    }


    fun isMissed() = diffCompletionTimeInMinutes() <= 0

    @SuppressLint("SimpleDateFormat")
    fun diffReminderTimeInMinutes(): Long {
        val formatter = SimpleDateFormat(TIME_FORMAT)
        val date = formatter.parse(reminderTime)
        val timeBeforeRemindInMinutes = date.hours * 60 + date.minutes
        if (date != null) {
            return diffCompletionTimeInMinutes() - timeBeforeRemindInMinutes
        }
        return 0
    }

    companion object {
        fun from(map: Map<String, Any>): Task {
            val constructor = Task::class.constructors.first()

            val args = constructor
                .parameters
                .map {
                    val anyArg = map.get(it.name)
                    if (anyArg is Long) {
                        it to anyArg.toInt()
                    } else {
                        it to map.get(it.name)
                    }
                }
                .toMap()
            return constructor.callBy(args)
        }

        fun reminderWorkId(id: Int) = "${id}RID"
        fun completionWorkId(id: Int) = "${id}CID"


    }

    fun reminderTaskId() = "${id}RID"
    fun completionTaskId() = "${id}CID"
}

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE finished = false")
    fun getUnfinishedTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE finished = true")
    fun getFinishedTasks(): Flow<List<Task>>



    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getTaskById(id: Int): Task?

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun trackTaskById(id: Int): Flow<Task?>

    @Delete
    fun delete(task: Task)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(task: Task):Long

}