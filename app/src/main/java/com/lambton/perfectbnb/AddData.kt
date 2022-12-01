package com.lambton.perfectbnb

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import kotlinx.android.synthetic.main.activity_add_data.*
import kotlinx.android.synthetic.main.activity_admin_main.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.header_layout.*
import java.io.ByteArrayOutputStream


class AddData : AppCompatActivity() {
   var ItemviewModel:ItemviewModel = ItemviewModel("","","","","")
    var selectedImage: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_data)
        setting.visibility= View.GONE
        txt_title.text="Add Place"
        imageView.setOnClickListener {
            val dialog: PickImageDialog =
                PickImageDialog.build(PickSetup()).show(this)
            PickImageDialog.build(PickSetup())
                .setOnPickResult { r ->

                    imageView.setImageURI(r.getUri())
                    selectedImage = r.getBitmap()
                    dialog.dismiss()
                }
                .setOnPickCancel {}.show(this)



        }
        add.setOnClickListener {
            val title=data_title.text.toString()
            val descrp=descp.text.toString()
            val  lat=lat.text.toString()
            val  lng=lng.text.toString()
            if(title.isBlank()){
                Toast.makeText(this,"Add Title", Toast.LENGTH_SHORT).show()

            }else if (descrp.isBlank()) {
                Toast.makeText(this, "Add Description", Toast.LENGTH_SHORT).show()
            }else if (lat.isBlank()){

                Toast.makeText(this, "Add Latitude", Toast.LENGTH_SHORT).show()

            }else if (lng.isBlank()){
                Toast.makeText(this, "Add Longitude", Toast.LENGTH_SHORT).show()

            }else{

                if(selectedImage==null){
                    Toast.makeText(this, "Image is required!", Toast.LENGTH_SHORT).show()
                }else{
                    uploadImageToFirebase(selectedImage!!,title,descrp,lat,lng)
                }

            }

        }
    }
    private fun saveAdminDataToFirebase(title:String, desc:String, lat:String, lng:String, image:String){
        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference

        ItemviewModel.Title=title
        ItemviewModel.Description=desc
        ItemviewModel.lat=lat
        ItemviewModel.lng=lng
        ItemviewModel.Image = image
        myRef.child("Places").push().setValue(ItemviewModel)


    }

    fun uploadImageToFirebase(bitmap: Bitmap,title:String,desc:String,lat:String,lng:String){


        val baos=ByteArrayOutputStream()
        val filename = java.util.UUID.randomUUID().toString()
        val storageRef: StorageReference = FirebaseStorage.getInstance().reference.child("places/"+filename+".jpg")
          bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos)
        val image=baos.toByteArray()
        val upload=storageRef.putBytes(image)
        upload.addOnCompleteListener{ uploadTask ->
            if(uploadTask.isSuccessful){
                storageRef.downloadUrl.addOnCompleteListener{ uriTask ->
                    uriTask.result.let {
                        //Toast.makeText(applicationContext,it.path,Toast.LENGTH_SHORT).show()
                        saveAdminDataToFirebase(title,desc,lat,lng,it.toString())
                        Toast.makeText(this, "Added Successfully", Toast.LENGTH_SHORT).show()
                        val intent= Intent(this,AdminMainActivity::class.java)
                        startActivity(intent)
                        finish()

                    }

                }
            }else
            {
               uploadTask.exception?.let {
                   Toast.makeText(applicationContext,it.message,Toast.LENGTH_SHORT).show()
               }
            }

        }
    }
}