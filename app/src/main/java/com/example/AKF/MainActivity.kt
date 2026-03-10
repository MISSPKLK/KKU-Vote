package com.example.project_election

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.project_election.navigation.AppNavGraph
import com.example.project_election.navigation.Routes
import com.example.project_election.ui.theme.Project_ElectionTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.Firebase
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private var globalNavController: NavHostController? = null

    private val signInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.idToken?.let { firebaseAuthWithGoogle(it) }
            } catch (e: ApiException) {
                Log.e("GoogleSignIn", "Google sign in failed", e)
                Toast.makeText(this, "การเข้าสู่ระบบล้มเหลว", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("993999132877-3bpo8nhps6nvoq8hi4c0pbv6kjabv66e.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        setContent {
            val navController = rememberNavController()
            globalNavController = navController

            val currentUser = auth.currentUser

            val startDest = if (currentUser != null) {
                if (currentUser.email == "admin@kku.ac.th") {
                    "AdminHomeScreen"
                } else if (currentUser.email?.endsWith("@kkumail.com") == true) {
                    Routes.HOME
                } else {
                    Routes.LOGIN
                }
            } else {
                Routes.LOGIN
            }

            Project_ElectionTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavGraph(
                        navController = navController,
                        startDestination = startDest,
                        onGoogleSignInClick = {
                            googleSignInClient.signOut().addOnCompleteListener {
                                signInLauncher.launch(googleSignInClient.signInIntent)
                            }
                        }
                    )
                }
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener { result ->
                val email = result.user?.email ?: ""

                if (email.endsWith("@kkumail.com")) {
                    globalNavController?.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                } else {
                    auth.signOut()
                    googleSignInClient.signOut()
                    Toast.makeText(this, "สำหรับนักศึกษามหาวิทยาลัยขอนแก่นเท่านั้น (@kkumail.com)", Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener {
                Log.e("GoogleSignIn", "Firebase auth failed: ${it.message}")
                Toast.makeText(this, "เข้าสู่ระบบไม่สำเร็จ", Toast.LENGTH_SHORT).show()
            }
    }


}