package com.yotharit.firebaseregis.user

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.yotharit.firebaseregis.R
import com.yotharit.firebaseregis.create_post.CreatePostActivity
import com.yotharit.firebaseregis.main.MainActivity
import com.yotharit.firebaseregis.view_location.ViewLocationActivity
import com.yotharit.firebaseregis.view_post.ViewPostActivity
import kotlinx.android.synthetic.main.user_layout.*

class UserActivity : AppCompatActivity() , View.OnClickListener {

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_layout)

        mAuth = FirebaseAuth.getInstance();

        setUpView()

    }

    fun setUpView() {
        signOutBtn.setOnClickListener(this)
        accountTextView.text = mAuth!!.currentUser!!.email
        postBtn.setOnClickListener(this)
        viewBtn.setOnClickListener(this)
        viewLoactionBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v) {
            signOutBtn -> signOut()
            postBtn -> postImage()
            viewBtn -> viewPost()
            viewLoactionBtn -> viewLoaction()
        }
    }

    private fun viewLoaction() {
        val intent = (Intent(this, ViewLocationActivity::class.java))
        startActivity(intent)    }

    fun viewPost() {
        val intent = (Intent(this, ViewPostActivity::class.java))
        startActivity(intent)
    }

    fun postImage() {
        val intent = (Intent(this, CreatePostActivity::class.java))
        startActivity(intent)
    }

    fun signOut() {
        mAuth!!.signOut()
        val intent = (Intent(this, MainActivity::class.java))
        startActivity(intent)
        finish()
    }



}