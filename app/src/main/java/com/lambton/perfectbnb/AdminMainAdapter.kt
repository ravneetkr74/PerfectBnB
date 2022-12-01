package com.lambton.perfectbnb

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

import java.util.*


public abstract class AdminMainAdapter(val context: Context,private val mlist:List<ItemviewModel>) :
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
        holder.title.text = ItemsViewModel.Title
        holder.desc.text=ItemsViewModel.Description
        val url: String? = ItemsViewModel.Image
        url?.let {
            Picasso.get().load(it).into(holder.imageView)
        }

        val geocoder: Geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address> =  geocoder.getFromLocation(
            ItemsViewModel.lat!!.toDouble(),
            ItemsViewModel.lng!!.toDouble(),
            1
        )
        val address: String = addresses[0].getAddressLine(0)
        holder.place.text=address
        holder.itemView.setOnClickListener {
            itemClickListener(position)
        }

    }

    override fun getItemCount(): Int {
        return mlist.size
    }
    abstract  fun itemClickListener(position: Int)
}