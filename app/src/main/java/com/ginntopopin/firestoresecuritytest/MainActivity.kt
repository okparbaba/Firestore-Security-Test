package com.ginntopopin.firestoresecuritytest

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var db:FirebaseFirestore
    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btRegister -> signUp()
            R.id.btLogin->logIn()
        }
    }

    private fun logIn() {
        progressBar?.visibility = VISIBLE
        val email = etUserName.text.toString()
        val password = etPassword.text.toString()
        if (!isValidEmail(email) or !TextUtils.isEmpty(password)){
            tvCredential.visibility = VISIBLE
            progressBar?.visibility = GONE
            return
        }
        mAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){
                task ->
                if (task.isSuccessful){
                    val user = mAuth.currentUser
                    updateUI(user)
                }else{
                    progressBar?.visibility = GONE
                    Toast.makeText(this@MainActivity, task.result.toString(),
                        Toast.LENGTH_SHORT).show()

                    updateUI(null)
                }
            }
    }

    private fun isValidEmail(target:CharSequence):Boolean{
        return if (TextUtils.isEmpty(target)){
            false
        }else{
            android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()

        }
    }
    private fun signUp() {
        progressBar?.visibility = VISIBLE
        val email = etUserName.text.toString()
        val password = etPassword.text.toString()
        if (!isValidEmail(email) or !TextUtils.isEmpty(password)){
            tvCredential.visibility = VISIBLE
            progressBar?.visibility = GONE
            return
        }
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                this
            ) {
                task ->
                if (task.isSuccessful){
                    val user = mAuth.currentUser
                    updateUI(user)
                }else{
                    progressBar?.visibility = GONE
                    Toast.makeText(this@MainActivity, task.result.toString(),
                        Toast.LENGTH_SHORT).show()

                    updateUI(null)
                }
            }
    }
    private fun addUser(user:String){
        val userData = HashMap<String,Any>()
        userData["user"] = user
        db.collection("users")
            .add(userData)
            .addOnSuccessListener {
                Toast.makeText(this@MainActivity,it.id,Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(this@MainActivity,it.message,Toast.LENGTH_LONG).show()
            }
    }


    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        btRegister.setOnClickListener(this)
        btLogin.setOnClickListener(this)
        tvCredential.visibility = GONE
        button3.setOnClickListener {
            addUser(editText3.text.toString())
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            // Signed in
            startActivity(Intent(this@MainActivity, OperationActivity::class.java))
            finish()
        } else {
            // Signed out

        }
    }
}
