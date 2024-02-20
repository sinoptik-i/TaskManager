package com.example.taskmanager.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskmanager.screens.support.ButtonRow
import com.example.taskmanager.screens.support.IOTextField
import com.example.taskmanager.viewmodels.AuthViewModel


@Composable
fun LogIn(
     authViewModel: AuthViewModel= hiltViewModel()
) {

    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        IOTextField(text = email, labelText = "Email")
        IOTextField(text = password, labelText = "Password")
        ButtonRow(text = "Войти") {
            authViewModel.logIn(email.value,password.value)
        }
    }
}

@Preview
@Composable
fun LogInPreview(){
    LogIn()
}