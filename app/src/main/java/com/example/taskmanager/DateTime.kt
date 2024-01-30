package com.example.taskmanager

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar

const val TIME_FORMAT = "HH.mm"
const val DATE_FORMAT = "dd.MM.yyyy HH.mm"
const val DATE_FORMAT_SECONDS = "dd.MM.yyyy HH.mm.ss"

@SuppressLint("SimpleDateFormat")
fun getNowTime(): String {
    val dateFormat = SimpleDateFormat(TIME_FORMAT)
    return dateFormat.format(Calendar.getInstance().timeInMillis)
}

@SuppressLint("SimpleDateFormat")
fun getNowDate(): String {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy")
    return dateFormat.format(Calendar.getInstance().timeInMillis)
}