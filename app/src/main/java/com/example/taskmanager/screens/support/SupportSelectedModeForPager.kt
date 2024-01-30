package com.example.taskmanager.screens.support

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.taskmanager.data.Task


class SupportSelectedModeForPager {

    val TAG = this.javaClass.simpleName
    var selectedMode = mutableStateOf(false)


    lateinit var items: List<Task>

    lateinit var mapItems: MutableMap<Task, MutableState<Boolean>>

    fun setWorks(works: List<Task>) {
        items = works
        mapItems = items.associateWith { mutableStateOf(false) }.toMutableMap()
    }


    fun getCheckState(work: Task) = mapItems.getOrPut(work) {
        mutableStateOf(false)
    }


    fun changeSelectOfItems(work: Task, isSelected: Boolean) {
        if (this::mapItems.isInitialized) {
            getCheckState(work).value = isSelected
        } else {
            Log.e(TAG, "mapItems is not Initialized")
        }
    }

    fun changeSelectedMode(work: Task) {
        selectedMode.value = !selectedMode.value
        if (selectedMode.value) {
            changeSelectOfItems(work, true)
        } else {
            mapItems.clear()
        }
    }

    fun offSelectedMode() {
        selectedMode.value = false
    }

    fun cancelAllSelections() {
        if (this::mapItems.isInitialized) {
            for (mapNote in mapItems) {
                changeSelectOfItems(mapNote.key, false)
            }
        } else {
            Log.e(TAG, "mapItems is not Initialized")
        }
    }

    fun getSelectedWorks(): List<Task> {
        if (this::mapItems.isInitialized) {
            return mapItems.filter { (key, value) ->
                value.value == true
            }.keys.toList()
        } else {
            Log.e(TAG, "mapItems is not Initialized")
            return emptyList()
        }
    }
}