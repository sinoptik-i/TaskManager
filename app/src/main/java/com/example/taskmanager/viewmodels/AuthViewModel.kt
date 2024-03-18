package com.example.taskmanager.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.screens.auth.PasswordHints
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


const val EXAMPLE_PASSWORD = "pass"

data class AuthState(
    // password/login
    val value: String = "",
    val hint: String = ""
)

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {

    val REGISTRATION = "Регистрация"
    val REGISTRY = "Зарегистрироваться"
    private val MIN_PASSWORD_LENGTH = 6

    private val TAG = this.javaClass.simpleName
    private val mAuth = FirebaseAuth.getInstance()


    //-----------------------------------------------------------------------
    // Если авторизован, переключаемся на меню обл бд -  SignedScreen
    val isSigned = MutableStateFlow(true)
    val authListener = FirebaseAuth.AuthStateListener {
        isSigned.value = (it.currentUser != null)
        /* Log.e(TAG, "authListener: ${it.currentUser}")
         Log.e(TAG, "isSigned2: ${isSigned2.value}")
         Log.e(TAG, "email: ${it.currentUser?.email}")*/
    }

    init {
        mAuth.addAuthStateListener(authListener)
    }

    private val logTrueRegFalse = MutableStateFlow(true)
    fun isLoginScreen() = logTrueRegFalse
    //------------------------------------------------------------

    //-----------------------------------------------------------------------
    //Переключение между состояниями авторизации
    fun setRegMode() {
        logTrueRegFalse.value = false
        regButtonTextFlow.value = REGISTRY
    }

    fun setLoginMode() {
        logTrueRegFalse.value = true
        regButtonTextFlow.value = REGISTRATION
    }
    //-----------------------------------------------------------------------


    //-----------------------------------------------------------------------
    //Вводимые пароли, их проверка
    private val _password1 = MutableStateFlow(AuthState(value = EXAMPLE_PASSWORD))
    fun getPassword1() = _password1.value

    fun setPassword1(password: String) {
        _password1.value = _password1.value.copy(value = password)
    }

    fun flowPassword1() = flowPassword(_password1)

    private val _password2 = MutableStateFlow(EXAMPLE_PASSWORD)
    fun getPassword2() = _password2.value

    fun setPassword2(password: String) {
        _password2.value = password
    }

    // fun flowPassword2Hint() = flowPasswordHint(_password2)

    val password2HintFlow = flowPassword(_password2)

    //доделать проверку символов
    fun flowPassword(passwordState: MutableStateFlow<AuthState>) = passwordState
        .flatMapConcat { password ->
            when {
                password.value.length < MIN_PASSWORD_LENGTH -> flow {
                    emit(password.copy(hint =  PasswordHints.FEW_CHARACTERS.message()))
                }

                !password.value.contains("1") -> flow {
                    emit(password.copy(hint = PasswordHints.INVALID_CHARACTERS.message()))
                }

                else -> flow {
                    emit(password.copy(hint = PasswordHints.VALID_PASSWORD.message()))
                }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            passwordState.value
        )

    //------------------------------------------------------------------------


    fun createUser(email: String, password: String, password2: String) {
        //   if (password2HintFlow.value == PasswordHints.VALID_PASSWORD.message()) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                it?.let {
                    Log.e(TAG, "$it")
                }
            }
            .addOnFailureListener {
                //The email address is already in use by another account.
                Log.e(TAG, "${it.message}")
                Log.e(TAG, "${it.localizedMessage}")
            }
        //  }
    }


    val regButtonTextFlow = MutableStateFlow(REGISTRATION)
    fun regButtonClick(email: String, password: String, password2: String) {
        if (logTrueRegFalse.value) {
            setRegMode()
        } else {
            setPassword1(password)
            setPassword2(password2)
            //  viewModelScope.launch {
            createUser(email, password, password2)
            //  }
        }
    }

    //---------------------------------------------------------------------------------
    //Login
    fun logIn(email: String, password: String): Boolean {
        var result = false
        setPassword1(password)
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                Log.e(TAG, "Login: $it ${it.isSuccessful}")
                result = true
            }
        return result
    }

    fun logOut() {
        mAuth.signOut()
        setLoginMode()
    }

    override fun onCleared() {
        mAuth.removeAuthStateListener(authListener)
        super.onCleared()
    }
}
/*


private fun passwordIsCorrect(password: String): Boolean {
    return when {
        password.length < MIN_PASSWORD_LENGTH -> false
        else -> true
    }
}

//suitability - соответствие
private fun checkPasswordsSuitability(password1: String, password2: String): Boolean {
    return when {
        password1 != password2 -> false
        !passwordIsCorrect(password1) -> false
        !passwordIsCorrect(password2) -> false
        else -> true
    }
}*/
