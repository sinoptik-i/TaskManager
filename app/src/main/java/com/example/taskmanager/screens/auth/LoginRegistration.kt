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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskmanager.screens.support.ButtonRow
import com.example.taskmanager.screens.support.IOTextField
import com.example.taskmanager.viewmodels.AuthViewModel

const val EXAMPLE_EMAIL = "qwe2@mail.ru"


@Composable
fun LoginRegistration(
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val logTrueRegFalseState = authViewModel.isLoginScreen().collectAsState()
    val regButtonTextState = authViewModel.regButtonTextFlow.collectAsState()
    //val regLogMessageState = authViewModel._regLogMessageState.collectAsState()


    val email = rememberSaveable { mutableStateOf(EXAMPLE_EMAIL) }

    /*val password = rememberSaveable { mutableStateOf(authViewModel.getPassword1().value) }
    val passwordHint = authViewModel.flowPassword1Hint().collectAsState()*/
    val password = rememberSaveable {
        mutableStateOf(authViewModel.flowPassword1().value)
    }

    /*val repeatPassword = rememberSaveable { mutableStateOf(authViewModel.getPassword2()) }
    val repeatPasswordHint = authViewModel.password2HintFlow.collectAsState()*/
    val repeatPassword = rememberSaveable { mutableStateOf(authViewModel.getPassword1().value) }
    val repeatPasswordHint = authViewModel.flowPassword1().collectAsState()


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {

        IOTextField(text = email, labelText = "Email")
        PasswordTextField(
            text = password.value,
            labelText = "Password",
            hintText = passwordHint.value
        )
        if (!logTrueRegFalseState.value) {
            PasswordTextField(
                text = repeatPassword,
                labelText = "Repeat password",
                hintText = repeatPasswordHint.value
            )
        }
        if (logTrueRegFalseState.value) {
            ButtonRow(text = "Войти") {
                authViewModel.logIn(email.value, password.value)
            }
        }
        val context = LocalContext.current
        ButtonRow(text = regButtonTextState.value) {
            authViewModel.regButtonClick(email.value, password.value, repeatPassword.value)
            /*      when {
                      regLogMessageState.value.content != null -> Toast.makeText(
                          context,
                          regLogMessageState.value.content,
                          Toast.LENGTH_LONG
                      ).show()
                  }*/

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