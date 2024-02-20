package com.example.taskmanager.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {

    val REGISTRATION = "Регистрация"
    val REGISTRY = "Зарегистрироваться"


    private val TAG = this.javaClass.simpleName
    private val mAuth = FirebaseAuth.getInstance()

    val isSigned2 = MutableStateFlow(true)

    val authListener = FirebaseAuth.AuthStateListener {
        isSigned2.value = (it.currentUser != null)
        Log.e(TAG, "authListener: ${it.currentUser}")
        Log.e(TAG, "isSigned2: ${isSigned2.value}")
        Log.e(TAG, "email: ${it.currentUser?.email}")
    }


    init {
        mAuth.addAuthStateListener(authListener)
    }


    private val logTrueRegFalse = MutableStateFlow(true)
    fun isLoginScreen() = logTrueRegFalse

    val isSigned: StateFlow<Boolean> =
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
            )

    fun createUser(email: String, password: String): Boolean {
        var result = false
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                Log.e(TAG, "reg: $it ${it.isSuccessful}")
                result = true
            }
        return result
    }

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

    fun setRegMode(){
        logTrueRegFalse.value = false
        regButtonTextFlow.value = REGISTRY
    }
    fun setLoginMode(){
        logTrueRegFalse.value = true
        regButtonTextFlow.value = REGISTRATION
    }

    val regButtonTextFlow = MutableStateFlow(REGISTRATION)
    fun regButtonClick(email: String, password: String) {
        if (logTrueRegFalse.value) {
            setRegMode()
        } else {
            createUser(email, password)
        }
    }

    override fun onCleared() {
        mAuth.removeAuthStateListener(authListener)
        super.onCleared()
    }
}