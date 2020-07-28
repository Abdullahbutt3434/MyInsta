package com.example.myinsta

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_signup.*
import java.util.zip.Inflater

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        signin_link_btn.setOnClickListener {
            startActivity(Intent(this,SigninActivity::class.java))
        }
        signup_btn.setOnClickListener {
            CreateAccount()
        }
    }

    private fun CreateAccount() {
        val fullName = fullname_signup.text.toString()
        val userName = username_signup.text.toString()
        val email = email_signup.text.toString()
        val password = pasword_signup.text.toString()


        when{
            TextUtils.isEmpty(fullName) -> Toast.makeText(this,"full name is required",Toast.LENGTH_LONG).show()
                TextUtils.isEmpty(email) -> Toast.makeText(this,"email name is required",Toast.LENGTH_LONG).show()
                TextUtils.isEmpty(userName) -> Toast.makeText(this,"user name is required",Toast.LENGTH_LONG).show()
                TextUtils.isEmpty(password) -> Toast.makeText(this,"password name is required",Toast.LENGTH_LONG).show()
                else->{
                val progressDialog = ProgressDialog(this@SignupActivity)
                progressDialog.setTitle("Sign up")
                progressDialog.setMessage("Please wait, this may take a while....")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()

                val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
                mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            saveUserInfo(fullName,userName,email,progressDialog)
                        }
                        else{
                            val massage = task.exception!!.toString()
                            Toast.makeText(this,"Error $massage",Toast.LENGTH_LONG).show()
                            mAuth.signOut()
                            progressDialog.dismiss()

                        }
                    }
            }
        }


    }

    private fun saveUserInfo(fullName: String, userName: String, email: String , progressDialog:ProgressDialog) {
        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        val userRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users")
        val userMap = HashMap<String , Any>()
        userMap["uid"]  = currentUserId
        userMap["fullname"] = fullName.toLowerCase()
        userMap["username"] = userName.toLowerCase()
        userMap["email"] = email
        userMap["bio"] = "i am using my instagram"
        userMap["image"]= "https://firebasestorage.googleapis.com/v0/b/instagram-clone-app-2a022.appspot.com/o/Default%20Images%2Fprofile.png?alt=media&token=bba90c92-92de-49a0-814e-6ed1314fbfa5"

        userRef.child(currentUserId).setValue(userMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    progressDialog.dismiss()
                    Toast.makeText(this,"Account has been created successfully",Toast.LENGTH_LONG).show()

                    FirebaseDatabase.getInstance().reference
                        .child("Follow").child(currentUserId)
                        .child("Following").child(currentUserId).setValue(true)

                    val intent = Intent(this@SignupActivity , MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }
                else{
                    val massage = task.exception!!.toString()
                    Toast.makeText(this,"Error $massage",Toast.LENGTH_LONG).show()
                    FirebaseAuth.getInstance().signOut()
                    progressDialog.dismiss()
                }
            }

    }

}