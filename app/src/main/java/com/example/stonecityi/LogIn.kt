package com.example.stonecityi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.stonecityi.databinding.ActivityLogInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.SignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class LogIn : AppCompatActivity() {
    //binding
    lateinit var binding : ActivityLogInBinding
    //campanion object
    companion object{
        private const val RC_SIGN_IN = 0
    }
    //firebase
    lateinit var auth : FirebaseAuth
    //firestore
    val db = FirebaseFirestore.getInstance()
    val collectionReference = db.collection("Users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_log_in)

        auth = FirebaseAuth.getInstance()
        val currentUser=auth.currentUser
        Log.i("LoginVajeed","${currentUser?.uid}")
        if(currentUser!=null){

            val intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.googleLogin.setOnClickListener{
            signIn()
        }

    }

    private fun signIn() {
        val gso= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient= GoogleSignIn.getClient(this,gso)
        val signInIntent=googleSignInClient.signInIntent

        startActivityForResult(signInIntent,RC_SIGN_IN)
    }

    override fun startActivityForResult(intent: Intent, requestCode: Int, options: Bundle?) {
        super.startActivityForResult(intent, requestCode, options)
    }
    override fun onActivityResult(requestCode:Int,resultCode:Int,data:Intent?){
        super.onActivityResult(requestCode,resultCode,data)

        if(requestCode==RC_SIGN_IN){
            val task=GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account=task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            }catch(e:ApiException){
                Toast.makeText(this,"Googlesigninfailed:${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential= GoogleAuthProvider.getCredential(idToken,null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this){task->
                if(task.isSuccessful){
                    val user=auth.currentUser
                    Toast.makeText(this,"Signedinas${user?.displayName}",Toast.LENGTH_SHORT).show()
                    collectionReference.whereEqualTo("userId",user?.uid).get().addOnSuccessListener {
                        if(!it.isEmpty) {
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }else{
                            startActivity(Intent(this,RegesterUser::class.java))
                            finish()
                        }
                    }.addOnFailureListener {
                        startActivity(Intent(this,RegesterUser::class.java))
                        finish()
                    }




                }else{
                    Toast.makeText(this,"Authenticationfailed",Toast.LENGTH_SHORT).show()
                }
            }

    }
}