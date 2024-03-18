/*
package com.example.taskmanager.viewmodels.unUsing

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.viewmodels.ContentState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class OldAuthViewModel @Inject constructor() : ViewModel() {

    val REGISTRATION = "Регистрация"
    val REGISTRY = "Зарегистрироваться"
    private val MIN_PASSWORD_LENGTH = 6

    private val TAG = this.javaClass.simpleName
    private val mAuth = FirebaseAuth.getInstance()

    val isSigned = MutableStateFlow(true)

    val authListener = FirebaseAuth.AuthStateListener {
        isSigned.value = (it.currentUser != null)
        */
/* Log.e(TAG, "authListener: ${it.currentUser}")
         Log.e(TAG, "isSigned2: ${isSigned2.value}")
         Log.e(TAG, "email: ${it.currentUser?.email}")*//*

    }
    val _regLogMessageState = MutableStateFlow(ContentState<String>(isInProgres = true))


    companion object {
        const val INCORRECT_PASSWORD = "Некорректный пароль."
        const val SUCCESSFUL_REGISTRATION = "Успешная регистрация."
        const val SWAP_TO_REG_MODE = "SWAP_TO_REG_MODE."
        const val YET_REGISTERED = "Вы уже регистрировались."
        const val UNKNOWN_REG_ERROR = "Неизвестная ошибка рагистрации."

    }


    init {
        mAuth.addAuthStateListener(authListener)
    }


    private val logTrueRegFalse = MutableStateFlow(true)
    fun isLoginScreen() = logTrueRegFalse

    */
/*  val isSigned: StateFlow<Boolean> =
          flowOf(mAuth.currentUser).flatMapLatest { currentUser ->
              flow {
                  Log.e(TAG, "currentUser: $currentUser")
                  if (currentUser != null) {
                      emit(true)
                  } else {
                      emit(false)
                  }
              }
          }
              .stateIn(
                  viewModelScope,
                  SharingStarted.WhileSubscribed(),
                  false
              )*//*



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
    }


    fun createUser(email: String, password: String, password2: String) {
        viewModelScope.launch {
            if (!checkPasswordsSuitability(password, password2)) {
                _regLogMessageState.value = ContentState(content = INCORRECT_PASSWORD)
            }
            else {
                try {
                    val res = mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            Log.e(TAG, "addOnCompleteListener: $it ${it.isSuccessful}")
                            _regLogMessageState.value =
                                ContentState(content = SUCCESSFUL_REGISTRATION)
                        }
                        .addOnFailureListener {
                            Log.e(TAG, "addOnFailureListener: $it ${it.message}")
                            _regLogMessageState.value = ContentState(content = YET_REGISTERED)
                        }
                    res.result?.let {
                        Log.e(TAG, "${it.additionalUserInfo}")
                    }
                    Log.e(TAG, "res.isSuccessful: ${res.isSuccessful}")
                } catch (error: Throwable) {
                    _regLogMessageState.value = ContentState(error = error)
                    Log.e(TAG, "res.isSuccessful: ${error.message}")
                }
            }
        }
    }

    */
/*

        fun createUser(email: String, password: String, password2: String): String {
            var result = ""
            if (!checkPasswordsSuitability(password, password2)) {
                return INCORRECT_PASSWORD
            }
            val a = mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    result = SUCCESSFUL_REGISTRATION
                    Log.e(TAG, "addOnCompleteListener: $it ${it.isSuccessful}")
                }
                .addOnFailureListener {
                    result = YET_REGISTERED
                    Log.e(TAG, "addOnFailureListener: $it ${it.message}")
                }

            Log.e(TAG, "createUser: $a ${a.result}")
            return result
        }

    *//*


    fun logIn(email: String, password: String): Boolean {
        var result = false
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

    fun setRegMode() {
        logTrueRegFalse.value = false
        regButtonTextFlow.value = REGISTRY
    }

    fun setLoginMode() {
        logTrueRegFalse.value = true
        regButtonTextFlow.value = REGISTRATION
    }

    val regButtonTextFlow = MutableStateFlow(REGISTRATION)
    fun regButtonClick(email: String, password: String, password2: String) {
        if (logTrueRegFalse.value) {
            setRegMode()
            //   _regLogMessageState.value=ContentState(content = SWAP_TO_REG_MODE)
        } else {
            viewModelScope.launch {  createUser(email, password, password2)}
        }
    }


    override fun onCleared() {
        mAuth.removeAuthStateListener(authListener)
        super.onCleared()
    }
}*/
