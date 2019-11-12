package com.ginntopopin.firestoresecuritytest

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_operation.*

class OperationActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db:FirebaseFirestore
    var email:String? = ""
    var uid:String? = ""
    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btAdd->{
                addUser(etUser.text.toString())
            }
        }
    }
    private fun signOut() {
        mAuth.signOut()
        startActivity(Intent(this@OperationActivity, MainActivity::class.java))
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_operation)
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        if (user!=null){
            email = user.email
            uid = user.uid
        }
        btAdd.setOnClickListener(this)


    }
    private fun addUser(user:String){
        val userData = HashMap<String,Any>()
        userData["user"] = user
        db.collection("users")
            .add(userData)
            .addOnSuccessListener {
                Toast.makeText(this@OperationActivity,it.id,Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(this@OperationActivity,it.message,Toast.LENGTH_LONG).show()
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==R.id.action_sign_out){
            signOut()
        }
        return super.onOptionsItemSelected(item)
    }
}
