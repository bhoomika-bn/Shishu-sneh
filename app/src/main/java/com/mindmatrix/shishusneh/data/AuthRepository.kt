package com.mindmatrix.shishusneh.data

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import com.google.firebase.auth.FirebaseAuth

class AuthRepository {
    private val auth = Firebase.auth

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    fun authState(): Flow<FirebaseUser?> = callbackFlow {
        val listener = com.google.firebase.auth.FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(firebaseAuth.currentUser)
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    suspend fun register(email: String, password: String): Result<Unit> = runCatching {
        auth.createUserWithEmailAndPassword(email.trim(), password).await()
    }

    suspend fun login(email: String, password: String): Result<Unit> = runCatching {
        auth.signInWithEmailAndPassword(email.trim(), password).await()
    }

    fun resetPassword(
        email: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        FirebaseAuth.getInstance()
            .sendPasswordResetEmail(email)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onError(exception.message ?: "Error")
            }
    }

    fun logout() {
        auth.signOut()
    }
}
