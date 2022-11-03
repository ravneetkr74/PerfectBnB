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


class ShowData : Fragment() {
    var cityname: String? = ""

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
        requireActivity().setting.visibility=View.VISIBLE
        requireActivity().setting.setOnClickListener {

            val builder = AlertDialog.Builder(requireContext(),R.style.CustomAlertDialog)
                .create()
            val view = layoutInflater.inflate(R.layout.showdatadialog,null)
            val  button = view.findViewById<ImageView>(R.id.cross)
            val  city = view.findViewById<TextView>(R.id.textView3)
            city.text=cityname

            builder.setView(view)
            button.setOnClickListener {
                builder.dismiss()
            }
            builder.setCanceledOnTouchOutside(false)
            builder.show()

        }
        return v
    }

    companion object {

    }
}