package com.example.myapp.viewModel



import android.app.Application
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapp.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _googleSignInStatus = MutableLiveData<Boolean>()
    val googleSignInStatus: LiveData<Boolean> get() = _googleSignInStatus
    private val googleSignInClient: GoogleSignInClient

    private val _emailExists = MutableLiveData<Boolean>()
    val emailExists: LiveData<Boolean> get() = _emailExists
    val registrationStatus = MutableLiveData<Boolean>()
    private val _loginStatus = MutableLiveData<Boolean>()
    val loginStatus: LiveData<Boolean> get() = _loginStatus
    var isEmailSent = false // To track whether an email has already been sent

    init {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(application.getString(R.string.default_web_client_id)) // Replace with your web client ID
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(application, gso)
    }

    fun registerUser(name: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = hashMapOf("name" to name, "email" to email)
                    firestore.collection("users").document(auth.currentUser!!.uid).set(user)
                        .addOnSuccessListener {
                            registrationStatus.value = true
                        }
                        .addOnFailureListener {
                            registrationStatus.value = false
                        }
                } else {
                    registrationStatus.value = false
                }
            }
    }

    fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Login successful
                    _loginStatus.value = true
                } else {
                    // Handle specific errors
                    when (task.exception) {
                        is FirebaseAuthInvalidUserException -> {
                            // The email does not exist
                            _loginStatus.value = false
                        }
                        is FirebaseAuthInvalidCredentialsException -> {
                            // The password is incorrect
                            _loginStatus.value = false
                        }
                        else -> {
                            _loginStatus.value = false
                        }
                    }
                }
            }
    }

    fun googleSignIn(launcher: ManagedActivityResultLauncher<Intent, ActivityResult>) {
        val signInIntent: Intent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    fun handleGoogleSignInResult(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account = task.result
            val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
            auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = hashMapOf(
                            "name" to account?.displayName,
                            "email" to account?.email,
                            "photo" to account?.photoUrl.toString()
                        )
                        firestore.collection("users").document(auth.currentUser!!.uid).set(user)
                            .addOnSuccessListener {
                                _loginStatus.value = true
                            }
                            .addOnFailureListener {
                                _loginStatus.value = false
                            }
                    } else {
                        _loginStatus.value = false
                    }
                }
        } else {
            _loginStatus.value = false
        }
    }

    fun checkUserEmail(email: String) {
        viewModelScope.launch {
            try {
                val querySnapshot = firestore.collection("users")
                    .whereEqualTo("email", email)
                    .get()
                    .await()
                _emailExists.value = !querySnapshot.isEmpty
            } catch (e: Exception) {
                _emailExists.value = false
            }
        }
    }

    var lastPasswordResetClickTime: Long = 0

    fun sendPasswordResetEmail(email: String, onResult: (Boolean, String?) -> Unit) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastPasswordResetClickTime < 2000) {
            onResult(false, "Please wait before requesting another password reset.")
            return
        }
        lastPasswordResetClickTime = currentTime
        viewModelScope.launch {
            try {
                auth.sendPasswordResetEmail(email).await()
                onResult(true, null)
            } catch (e: FirebaseTooManyRequestsException) {
                onResult(false, "Too many requests. Please try again later.")
            } catch (e: Exception) {
                onResult(false, e.message)
            }
        }
    }

    fun updateUserPassword(oldPassword: String, newPassword: String, onResult: (Boolean) -> Unit) {
        val user = auth.currentUser
        if (user != null) {
            val credential = EmailAuthProvider.getCredential(user.email!!, oldPassword)
            user.reauthenticate(credential).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    user.updatePassword(newPassword).addOnCompleteListener { updateTask ->
                        if (updateTask.isSuccessful) {
                            onResult(true)
                        } else {
                            onResult(false)
                        }
                    }
                } else {
                    onResult(false)
                }
            }
        } else {
            onResult(false)
        }
    }


}
