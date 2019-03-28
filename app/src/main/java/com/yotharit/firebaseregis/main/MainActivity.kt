package com.yotharit.firebaseregis.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.yotharit.firebaseregis.R
import com.yotharit.firebaseregis.user.UserActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), View.OnClickListener {


    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance();
        setUpView()
    }

    fun setUpView() {
        hideLoader()
        signInBtn.setOnClickListener(this)
        registerBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            signInBtn -> signIn()
            registerBtn -> register()
        }
    }

    fun showLoader() {
        loader.visibility = View.VISIBLE
        group.visibility = View.GONE
    }

    fun hideLoader() {
        loader.visibility = View.GONE
        group.visibility = View.VISIBLE
    }

    fun signIn() {
        showLoader()
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        if (email != null && password != null) {
            mAuth!!.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Sign in success", Toast.LENGTH_LONG).show()
                        val intent = (Intent(this, UserActivity::class.java))
                        startActivity(intent)
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this, "Sign in fail", Toast.LENGTH_LONG)
                        hideLoader()
                    }
                }
        } else {
            Toast.makeText(this, "Enter username/password", Toast.LENGTH_LONG).show()
        }

    }

    fun register() {
        showLoader()
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        if (email != null && password != null) {
            mAuth!!.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Register success", Toast.LENGTH_LONG).show()
                        hideLoader()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this, "Register fail", Toast.LENGTH_LONG).show()
                        hideLoader()
                    }
                }
        } else {
            Toast.makeText(this, "Enter username/password", Toast.LENGTH_LONG).show()
        }
    }


}
