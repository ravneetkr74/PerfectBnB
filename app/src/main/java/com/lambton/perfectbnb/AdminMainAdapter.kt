package com.lambton.perfectbnb

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*


class AdminMainAdapter(val Context: Context,private val mlist:List<ItemviewModel>) :
    RecyclerView.Adapter<AdminMainAdapter.ViewHolder>() {


    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image)
        val title: TextView = itemView.findViewById(R.id.toolTitle)
        val desc: TextView = itemView.findViewById(R.id.txtDesc)
        val place: TextView = itemView.findViewById(R.id.txtplace)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adminmain_itemlayout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ItemsViewModel = mlist[position]

        // sets the image to the imageview from our itemHolder class
//        holder.imageView.setImageResource(ItemsViewModel.image)
//
//        // sets the text to the textview from our itemHolder class
        holder.title.text = ItemsViewModel.Title
        holder.desc.text=ItemsViewModel.Description


        val geocoder: Geocoder
        val addresses: List<Address>
        geocoder = Geocoder(Context, Locale.getDefault())

        addresses = geocoder.getFromLocation(
            ItemsViewModel.lat!!.toDouble(),
            ItemsViewModel.lng!!.toDouble(),
            1
        ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5


        val address: String = addresses[0].getAddressLine(0)
        holder.place.text=address

    }

    override fun getItemCount(): Int {
        return mlist.size
    }
}