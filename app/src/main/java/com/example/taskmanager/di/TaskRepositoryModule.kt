package com.example.taskmanager.di

import android.content.Context
import com.example.taskmanager.data.TaskDatabase
import com.example.taskmanager.data.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Module
@InstallIn(SingletonComponent::class)
object TaskRepositoryModule {

    @Provides
    fun provideNoteDao(
        @ApplicationContext context: Context
    ) =
        TaskDatabase.getDatabase(context).taskDao()

    @Provides
    fun provideCoroutineScope() =
        CoroutineScope(Dispatchers.IO + SupervisorJob())

   /* @Provides
    fun repository() = TaskRepository(provideNoteDao(
        context =
    ), provideCoroutineScope())*//* TaskRepository(provideNoteDao(),
        provideCoroutineScope()
    )*/
}