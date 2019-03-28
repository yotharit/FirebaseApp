package com.yotharit.firebaseregis.view_post

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.yotharit.firebaseregis.R
import kotlinx.android.synthetic.main.view_post_layout.*


class ViewPostActivity : AppCompatActivity() {

    lateinit var adapter : ViewPostRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_post_layout)
        setUpView()
        loadData()
    }

    fun loadData() {
        val query = FirebaseDatabase.getInstance()
            .reference
            .child("User_Details")


        val options = FirebaseRecyclerOptions.Builder<PostModal>()
            .setQuery(query) { snapshot ->
                PostModal(
                    snapshot.child("Image_Title").value!!.toString(),
                    snapshot.child("Image_URL").value!!.toString()
                )
            }
            .build()

        adapter = ViewPostRecyclerViewAdapter(options)
        recycleView.adapter = adapter
    }

    fun setUpView() {
        var linearLayoutManager: LinearLayoutManager = LinearLayoutManager(this)
        recycleView.layoutManager = linearLayoutManager
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

}