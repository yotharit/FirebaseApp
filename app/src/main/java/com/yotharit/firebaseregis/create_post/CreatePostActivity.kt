package com.yotharit.firebaseregis.create_post

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.yotharit.firebaseregis.R
import kotlinx.android.synthetic.main.create_post_layout.*


class CreatePostActivity : AppCompatActivity(), View.OnClickListener {

    private var mStorageRef: StorageReference? = null
    private var mDataBaseReference: DatabaseReference? = null
    private var mRootRef: DatabaseReference? = null
    private var mImageUri: Uri? = null

    private val READ_EXTERNAL_STORAGE = 0
    private val GALLERY_INTENT = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_post_layout)
        setUpFireBase()
        setUpView()

    }

    fun setUpFireBase() {
        mDataBaseReference = FirebaseDatabase.getInstance().reference
        mRootRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mobiledevandroid-771c0.firebaseio.com/")
            .child("User_Details").push()
        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://mobiledevandroid-771c0.appspot.com")

    }

    fun setUpView() {
        hideLoader()
        selectBtn.setOnClickListener(this)
        postBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            selectBtn -> selectImage()
            postBtn -> postGallery()
        }
    }

    fun selectImage() {
        if (checkPermissionForReadExtertalStorage() == false) {
            requestPermissionForReadExtertalStorage()
            openGallery()
        }
    }

    fun checkPermissionForReadExtertalStorage(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val result = applicationContext.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            return result == PackageManager.PERMISSION_GRANTED
        }
        return false
    }

    @Throws(Exception::class)
    fun requestPermissionForReadExtertalStorage() {
        try {
            ActivityCompat.requestPermissions(
                this as Activity, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_EXTERNAL_STORAGE
            )
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    fun postGallery() {
        var postInfo = postEditText.text.toString().trim()

        if(!postInfo.isEmpty()) {
            var childRef_name = mRootRef!!.child("Image_Title")
            childRef_name.setValue(postInfo)
            Toast.makeText(this,"Updated Title",Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this,"Please enter title!",Toast.LENGTH_LONG).show()
        }
    }

    fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setType("image/*")
        startActivityForResult(intent, GALLERY_INTENT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY_INTENT && resultCode == Activity.RESULT_OK) {
            mImageUri = data!!.data
            val filePath = mStorageRef!!.child("User_Images").child(mImageUri!!.lastPathSegment!!)
            showLoader()
            var downloadUri: Uri? = null

            filePath.putFile(mImageUri!!).addOnCompleteListener() {
                Toast.makeText(this, "Upload success", Toast.LENGTH_LONG).show()
            }.addOnCompleteListener {
                filePath.downloadUrl.addOnSuccessListener {
                    downloadUri = it!!
                    mRootRef!!.child("Image_URL").setValue(downloadUri.toString())

                    //Change to glide
                    Glide.with(this).load(downloadUri.toString()).into(imageView)
                    hideLoader()
                }.addOnFailureListener {
                    Toast.makeText(this, "url fail", Toast.LENGTH_LONG).show()
                }
            }
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


}