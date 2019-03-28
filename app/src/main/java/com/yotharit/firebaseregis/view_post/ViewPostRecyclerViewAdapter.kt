package com.yotharit.firebaseregis.view_post

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.yotharit.firebaseregis.R
import android.R.string.cancel
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog


class ViewPostRecyclerViewAdapter(var option : FirebaseRecyclerOptions<PostModal>) : FirebaseRecyclerAdapter<PostModal,ViewPostRecyclerViewAdapter.MyViewHolder>(option) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_post_list_view, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(p0: MyViewHolder, p1: Int, p2: PostModal) {
        p0.textView.text = p2.text
        Glide.with(p0.itemView).load(p2.imageUrl).into(p0.imageView)
        p0.itemView.setOnClickListener({
            val builder = AlertDialog.Builder(p0.itemView.context)
            builder.setMessage("Delete?").setCancelable(false)
                .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                    val selectedItems = p1
                    this.getRef(selectedItems).removeValue()
                    this.notifyItemRemoved(selectedItems)
                    this.notifyDataSetChanged()
                })
                .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
            val dialog = builder.create()
            dialog.setTitle("Are you sure?")
            dialog.show()
        })
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imageView : ImageView = view.findViewById(R.id.imageView)
        var textView : TextView = view.findViewById(R.id.textView)
    }

}