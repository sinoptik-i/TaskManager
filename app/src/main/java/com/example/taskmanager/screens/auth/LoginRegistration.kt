package com.example.taskmanager.screens.auth

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskmanager.screens.support.ButtonRow
import com.example.taskmanager.screens.support.IOTextField
import com.example.taskmanager.viewmodels.AuthViewModel

const val EXAMPLE_EMAIL = "qwe@mail.ru"
const val EXAMPLE_PASSWORD = "passwordQWE1"

@Composable
fun LoginRegistration(
    authViewModel: AuthViewModel = hiltViewModel()
) {

    val logTrueRegFalseState = authViewModel.isLoginScreen().collectAsState()
    val regButtonTextState = authViewModel.regButtonTextFlow.collectAsState()

    val email = rememberSaveable { mutableStateOf(EXAMPLE_EMAIL) }
    val password = rememberSaveable { mutableStateOf(EXAMPLE_PASSWORD) }
    val repeatPassword = rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {

        IOTextField(text = email, labelText = "Email")
        IOTextField(text = password, labelText = "Password")

        if (!logTrueRegFalseState.value) {
            IOTextField(text = repeatPassword, labelText = "Repeat password")
        }
        if (logTrueRegFalseState.value) {
            ButtonRow(text = "Войти") {
                authViewModel.logIn(email.value, password.value)
            }
        }
        ButtonRow(text = regButtonTextState.value) {
            authViewModel.regButtonClick(email.value, password.value)
        }
    }
    BackHandler(enabled = !logTrueRegFalseState.value) {
        authViewModel.setLoginMode()
    }
}

@Preview
@Composable
fun RegistrationPreview() {
    LoginRegistration()
}