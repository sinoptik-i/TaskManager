package com.example.taskmanager.screens.drawer

import androidx.activity.compose.BackHandler
import androidx.compose.material.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskmanager.screens.auth.LoginRegistration
import com.example.taskmanager.screens.support.ButtonRow
import com.example.taskmanager.viewmodels.AllTasksViewModel
import com.example.taskmanager.viewmodels.AuthViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerMenu(
    drawerState: DrawerState,
    onBack: () -> Unit = {},
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val authDrawerState = authViewModel.isSigned2.collectAsState()

    if (authDrawerState.value) {
        SignedScreen()
    } else {
        LoginRegistration()
    }

    BackHandler(
        enabled = drawerState.isOpen
    ) {
        onBack()
    }
}

