package com.lambton.perfectbnb

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.annotations.Nullable
import kotlinx.android.synthetic.main.activity_add_data.*
import kotlinx.android.synthetic.main.activity_admin_main.*
import kotlinx.android.synthetic.main.header_layout.*


class AdminMainActivity : AppCompatActivity() {

    var updatedList=ArrayList<ItemviewModel>()
    private lateinit var adapter: AdminMainAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_main)
        setting.visibility= View.VISIBLE
       setting.setImageResource(R.drawable.ic_baseline_add_circle_24)
        txt_title.text="All Places"
        setting.setOnClickListener {

            val intent= Intent(this,AddData::class.java)
            startActivity(intent)
            finish()

        }
        initializeListView()




    }

    fun initializeListView(){

        recycler.visibility=View.VISIBLE
        layout.visibility=View.GONE
        recycler.layoutManager = LinearLayoutManager(this)
        adapter = AdminMainAdapter(this,updatedList)


        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference
        val firebaseAuth= FirebaseAuth.getInstance()

        myRef.child("Places").addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(
                snapshot: DataSnapshot,
                @Nullable previousChildName: String?
            ) {
                // this method is called when new child is added to
                // our data base and after adding new child
                // we are adding that item inside our array list and
                // notifying our adapter that the data in adapter is changed.
                updatedList.add(snapshot.getValue(com.lambton.perfectbnb.ItemviewModel::class.java)!!)
                Log.e("@#@","get list value"+updatedList.size+updatedList.get(0).Title)
                adapter.notifyDataSetChanged()
            }

            override fun onChildChanged(
                snapshot: DataSnapshot,
                @Nullable previousChildName: String?
            ) {
                // this method is called when the new child is added.
                // when the new child is added to our list we will be
                // notifying our adapter that data has changed.
                adapter.notifyDataSetChanged()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                // below method is called when we remove a child from our database.
                // inside this method we are removing the child from our array list
                // by comparing with it's value.
                // after removing the data we are notifying our adapter that the
                // data has been changed.
                updatedList.remove(snapshot.getValue(com.lambton.perfectbnb.ItemviewModel::class.java))
                adapter.notifyDataSetChanged()
            }

            override fun onChildMoved(
                snapshot: DataSnapshot,
                @Nullable previousChildName: String?
            ) {
                // this method is called when we move our
                // child in our database.
                // in our code we are note moving any child.
            }

            override fun onCancelled(error: DatabaseError) {
                // this method is called when we get any
                // error from Firebase with error.
            }
        })
        Log.e("@#@","get list"+updatedList.size)


        recycler.adapter = adapter

    }

}