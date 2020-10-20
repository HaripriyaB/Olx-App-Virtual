package com.haripriya.olxapplication.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.haripriya.olxapplication.BaseActivity
import com.haripriya.olxapplication.MainActivity
import com.haripriya.olxapplication.R
import com.haripriya.olxapplication.utilities.Constants
import com.haripriya.olxapplication.utilities.SharedPref
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*


class LoginActivity : BaseActivity() {

    private var callbackManager: CallbackManager? = null
    private var auth: FirebaseAuth? = null
    private val RC_SIGN_IN: Int = 100
    private var gso: GoogleSignInOptions? = null
    private var googleSignInClient: GoogleSignInClient? = null

    private val EMAIL = "email"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        setContentView(R.layout.activity_login)
        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();
        //Code to get KeyHash for fb login setup
//        try {
//            val info = packageManager.getPackageInfo(
//                packageName,
//                PackageManager.GET_SIGNATURES
//            )
//            for (signature in info.signatures) {
//                val md: MessageDigest = MessageDigest.getInstance("SHA")
//                md.update(signature.toByteArray())
//                Log.e(
//                    "MY KEY HASH:",
//                    Base64.encodeToString(md.digest(), Base64.DEFAULT)
//                )
//            }
//        } catch (e: PackageManager.NameNotFoundException) {
//            e.printStackTrace()
//        } catch (e: NoSuchAlgorithmException) {
//            e.printStackTrace()
//        }

        login_button.setReadPermissions(Arrays.asList(EMAIL))
        clickListeners()
        configureGoogleSignin()
        registerFbCallback()
    }

    private fun registerFbCallback() {
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    val credential =
                        FacebookAuthProvider.getCredential(loginResult?.accessToken.toString())
                    auth?.signInWithCredential(credential)?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val account = auth?.currentUser
                            if (account?.email != null) {
                                SharedPref(applicationContext).setString(
                                    Constants.USER_EMAIL,
                                    account.email!!
                                )
                            }
                            if (account?.uid != null) {
                                SharedPref(applicationContext).setString(
                                    Constants.USER_ID,
                                    account.uid
                                )
                            }
                            if (account?.displayName != null) {
                                SharedPref(applicationContext).setString(
                                    Constants.USER_NAME,
                                    account.displayName!!
                                )
                            }
                            if (account?.photoUrl != null) {
                                SharedPref(applicationContext).setString(
                                    Constants.USER_PHOTO,
                                    account.photoUrl.toString()
                                )
                            }
                            Toast.makeText(
                                this@LoginActivity,
                                "Welcome " + account?.displayName,
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(
                                baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                }

                override fun onCancel() {
                    // App code
                }

                override fun onError(exception: FacebookException) {
                    // App code
                }
            })
    }

    private fun clickListeners() {
        fb_login.setOnClickListener {
            login_button.performClick()
        }
        go_login.setOnClickListener {
            googleSignIn()
        }

    }

    private fun googleSignIn() {
        val signInIntent = googleSignInClient?.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)

    }

    private fun configureGoogleSignin() {
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso!!)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("Login", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("Login", "Google sign in failed", e)
                // ...
            }
        } else {
            callbackManager?.onActivityResult(requestCode, resultCode, data);
        }

    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth!!.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                if (account.email != null) {
                    SharedPref(this).setString(Constants.USER_EMAIL, account.email!!)
                }
                if (account.id != null) {
                    SharedPref(this).setString(Constants.USER_ID, account.id!!)
                }
                if (account.displayName != null) {
                    SharedPref(this).setString(Constants.USER_NAME, account.displayName!!)
                }
                if (account.photoUrl != null) {
                    SharedPref(this).setString(Constants.USER_PHOTO, account.photoUrl.toString())
                }
                Toast.makeText(this, "Welcome " + account.displayName, Toast.LENGTH_LONG).show()
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Sign in failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}