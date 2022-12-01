package com.lambton.perfectbnb.user

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.annotations.Nullable
import com.lambton.perfectbnb.R
import com.lambton.perfectbnb.admin.AdminMainAdapter
import com.lambton.perfectbnb.models.ItemviewModel
import com.lambton.perfectbnb.models.ModalClass
import com.lambton.perfectbnb.network.ApiUtilities
import kotlinx.android.synthetic.main.activity_admin_main.*
import kotlinx.android.synthetic.main.header_layout.*

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ShowData : Fragment() {
    var cityname: String? = ""
    var updatedList=ArrayList<ItemviewModel>()
    private lateinit var adapter: AdminMainAdapter
    var latitude:String="0.0"
    var longitude:String ="0.0"
    lateinit var recyclerview:RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v: View = inflater.inflate(R.layout.fragment_show_data, container, false)
        recyclerview=v.findViewById(R.id.recyclerview)
        setupUI()
        //Back button
        requireActivity().backButton.setOnClickListener {
            //On click of back button opening Maps Fragment
            val fragment = MapsFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame_container, fragment).commit()
        }
        requireActivity().setting.setOnClickListener {
            //Creating a popup which show up on click of info button.
            val builder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
                .create()
            val view = layoutInflater.inflate(R.layout.showdatadialog,null)
            val  button = view.findViewById<ImageView>(R.id.cross)
            val  city = view.findViewById<TextView>(R.id.textView3)
            val temp=view.findViewById<TextView>(R.id.temperature)
            city.text=cityname
            showWeatherData(temp)
            builder.setView(view)
            button.setOnClickListener {
                builder.dismiss()
            }
            builder.setCanceledOnTouchOutside(false)
            builder.show()

        }
        initializeListView()
        return v
    }
    private fun setupUI() {
        cityname = arguments?.getString("city")
        latitude = arguments?.getString("lat").toString()
        longitude = arguments?.getString("lng").toString()
        requireActivity().logoutButton.visibility = View.GONE
        requireActivity().backButton.visibility = View.VISIBLE
        requireActivity().setting.visibility = View.VISIBLE
    }
    //Calling Weather API (Sending latitude, longitude and API-Key)
    private fun showWeatherData(textView: TextView) {
        ApiUtilities.getApiInterface()?.getCurrentWeatherData(latitude, longitude, APIKEY, "metric")
            ?.enqueue(object : Callback<ModalClass> {
                override fun onResponse(call: Call<ModalClass>, response: Response<ModalClass>) {
                    //Got response from API and setting it to textView.
                    if (response.isSuccessful) {
                        textView.text = response.body()?.main?.temp.toString() + " °C"
                    }
                }
                override fun onFailure(call: Call<ModalClass>, t: Throwable) {
                    Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

    fun initializeListView(){

        recyclerview.visibility=View.VISIBLE

        recyclerview.layoutManager = LinearLayoutManager(requireContext())
        adapter = object : AdminMainAdapter(requireContext(), updatedList) {
            override fun itemClickListener(position: Int) {

                Toast.makeText(requireContext(),"get click"+position,Toast.LENGTH_SHORT).show()
            }
        }



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
                updatedList.add(snapshot.getValue(ItemviewModel::class.java)!!)
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
                updatedList.remove(snapshot.getValue(ItemviewModel::class.java))
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

    companion object {
        const val APIKEY="881ca47ef874c2b67cad0cfdd95b95e9"
    }
}
