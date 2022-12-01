package com.lambton.perfectbnb

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.header_layout.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.Array
import kotlin.Boolean
import kotlin.Int
import kotlin.IntArray
import kotlin.String
import kotlin.apply
import kotlin.arrayOf
import kotlin.let


class MapsFragment : Fragment() {

    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val permissionId = 2
     var lat: String="-34.0"
     var lng: String="151.0"
    var flag:Boolean=false
    lateinit var locality:String
    lateinit var mapFragment:SupportMapFragment
   private lateinit var locationRequest:com.google.android.gms.location.LocationRequest
    private lateinit var locationCallback: LocationCallback
    lateinit var show_status:TextView
    lateinit var locate_me:Button

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        val latLng = LatLng(lat.toDouble(), lng.toDouble())
        if(flag) {
            googleMap.addMarker(MarkerOptions().position(latLng))
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10f)
            googleMap.animateCamera(cameraUpdate)
        }
    }

    override fun onCreateView(


        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState);
        val v: View = inflater.inflate(R.layout.fragment_maps, container, false)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        locationRequest = LocationRequest.create().apply{
            // Sets the desired interval for
            // active location updates.
            // This interval is inexact.
            var interval = TimeUnit.SECONDS.toMillis(60)

            // Sets the fastest rate for active location updates.
            // This interval is exact, and your application will never
            // receive updates more frequently than this value
             var fastestInterval = TimeUnit.SECONDS.toMillis(30)

            // Sets the maximum time when batched location
            // updates are delivered. Updates may be
            // delivered sooner than this interval
             var maxWaitTime = TimeUnit.MINUTES.toMillis(2)

             var priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }


         locate_me = v.findViewById<Button>(R.id.locate_me)
         show_status=v.findViewById<TextView>(R.id.show_status)
         mapFragment = (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)!!
        mapFragment?.getMapAsync(callback)
        locate_me.setOnClickListener(View.OnClickListener {
            if(locate_me.text.equals("Start Search")){

                val fragment = ShowData()
                val bundle = Bundle()
                bundle.putString("city",locality)
                bundle.putString("lat",lat)
                bundle.putString("lng",lng)
                fragment.arguments=bundle
                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()

            }else {
                getLocation()
            }
        })

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                p0?.lastLocation?.let {
                    //currentLocation = locationByGps
                    lat = p0.lastLocation.latitude.toString()
                    lng = p0.lastLocation.longitude.toString()
                    Log.e("@#@","get lat"+lat)
                    Log.e("@#@","get lng"+lng)
                    // use latitude and longitude as per your need
                    val geocoder = Geocoder(requireContext(), Locale.getDefault())
                    val list: List<Address> =
                        geocoder.getFromLocation(p0.lastLocation.latitude, p0.lastLocation.longitude, 1)
                    locality = "${list[0].locality}"
                    saveLocationToFirebase(lat,lng)
                    flag=true
                    show_status.visibility=View.VISIBLE
                    locate_me.text="Start Search"
                    mapFragment?.getMapAsync(callback)
                    val removeTask = mFusedLocationClient.removeLocationUpdates(locationCallback)
                    removeTask.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("TAG", "Location Callback removed.")
                        } else {
                            Log.d("TAG", "Failed to remove Location Callback.")
                        }
                    }

                } ?: {
                    Log.d("TAG", "Location information isn't available.")
                }
            }
        }
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        val geocoder = Geocoder(requireContext(), Locale.getDefault())
                        val list: List<Address> =
                            geocoder.getFromLocation(location.latitude, location.longitude, 1)

                        lat = list[0].latitude.toString()
                        Log.e(
                            "@#@#", "get Latitude\n" +
                                    "${list[0].latitude}"
                        )
                        lng = list[0].longitude.toString()
                        Log.e(
                            "@#@#", "get Longitude\n" +
                                    "${list[0].longitude}"
                        )
                        var name = "Country Name\n${list[0].countryName}"
                        Log.e(
                            "@#@#", "get Country Name\n" +
                                    "${list[0].countryName}"
                        )
                        locality = "${list[0].locality}"
                        Log.e(
                            "@#@#", "get Locality\n" +
                                    "${list[0].locality}"
                        )
                        var address = "Address\n${list[0].getAddressLine(0)}"
                        Log.e(
                            "@#@#", "get Address\n" +
                                    "${list[0].getAddressLine(0)}"
                        )

                        saveLocationToFirebase(lat,lng)
                        flag=true
                        show_status.visibility=View.VISIBLE
                        locate_me.text="Start Search"
                        mapFragment?.getMapAsync(callback)
                    }else{

                        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback,
                            Looper.myLooper()!!
                        )

                    }
                }
            } else {
                Toast.makeText(requireContext(), "GPS is Disabled,Please enable it", Toast.LENGTH_LONG)
                    .show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            permissionId
        )
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            }
        }

    }
    fun saveLocationToFirebase(lat:String,lng:String){
        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference
       val firebaseAuth= FirebaseAuth.getInstance()

        val locationData
                = HashMap<String, Any> ()
        locationData.put("lat",lat)
        locationData.put("long",lng)
        myRef.child("Users").child(firebaseAuth.currentUser!!.uid).updateChildren(locationData)
    }
}