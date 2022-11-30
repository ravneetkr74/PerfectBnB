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
    var cityname: String? = ""

    val URL:String="https://api.openweathermap.org/data/2.5/weather?"
    var latitude:String="0.0"
    var longitude:String ="0.0"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v: View = inflater.inflate(R.layout.fragment_show_data, container, false)
        cityname = arguments?.getString("city")
        latitude=arguments?.getString("lat").toString()
        longitude= arguments?.getString("lng").toString()

        requireActivity().setting.visibility=View.VISIBLE
        requireActivity().setting.setOnClickListener {

            val builder = AlertDialog.Builder(requireContext(),R.style.CustomAlertDialog)
                .create()
            val view = layoutInflater.inflate(R.layout.showdatadialog,null)
            val  button = view.findViewById<ImageView>(R.id.cross)
            val  city = view.findViewById<TextView>(R.id.textView3)
            val temp=view.findViewById<TextView>(R.id.temperature)
            city.text=cityname
            showWeatherData(latitude,longitude,temp)
            builder.setView(view)
            button.setOnClickListener {
                builder.dismiss()
            }
            builder.setCanceledOnTouchOutside(false)
            builder.show()

        }
        return v
    }

    private fun showWeatherData(lat:String,long:String,textView: TextView){

        ApiUtilities.getApiInterface()?.getCurrentWeatherData(latitude,longitude, APIKEY,"metric")?.enqueue(object : Callback<ModalClass>{
            override fun onResponse(call: Call<ModalClass>, response: Response<ModalClass>) {
                if(response.isSuccessful){
                    textView.text= response.body()?.main?.temp.toString()+" °C"
                }
            }

            override fun onFailure(call: Call<ModalClass>, t: Throwable) {
               Toast.makeText(requireContext(),"Something went wrong",Toast.LENGTH_SHORT).show()
            }

        })

    }



    companion object {
        const val APIKEY="881ca47ef874c2b67cad0cfdd95b95e9"
    }
}