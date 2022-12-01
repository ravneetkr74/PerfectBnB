package com.lambton.perfectbnb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.header_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ShowData : Fragment() {
    private var cityname: String? = ""
    private var latitude: String = "0.0"
    private var longitude: String = "0.0"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v: View = inflater.inflate(R.layout.fragment_show_data, container, false)
        setupUI()
        //Back button
        requireActivity().backButton.setOnClickListener {
            //On click of back button opening Maps Fragment
            val fragment = MapsFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame_container, fragment).commit()
        }
        //Creating a popup which show up on click of info button.
        requireActivity().setting.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
                .create()
            val view = layoutInflater.inflate(R.layout.showdatadialog, null)
            val button = view.findViewById<ImageView>(R.id.cross)
            val city = view.findViewById<TextView>(R.id.textView3)
            val temp = view.findViewById<TextView>(R.id.temperature)
            city.text = cityname
            showWeatherData(temp)
            builder.setView(view)
            button.setOnClickListener {
                builder.dismiss()
            }
            builder.setCanceledOnTouchOutside(false)
            builder.show()
        }
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


    companion object {
        const val APIKEY = "881ca47ef874c2b67cad0cfdd95b95e9"
    }
}