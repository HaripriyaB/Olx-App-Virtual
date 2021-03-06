package com.haripriya.olxapplication.ui.browseCategory.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.haripriya.olxapplication.R
import com.haripriya.olxapplication.models.DataItemModel

import java.text.SimpleDateFormat

class BrowseCategoryAdapter(var dataItemModelList: MutableList<DataItemModel>,
                            var mClickListener: ItemClickListener)
    : RecyclerView.Adapter<BrowseCategoryAdapter.ViewHolder>() {
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_browse_category,parent, false)
        return ViewHolder(viewHolder)
    }

    override fun getItemCount(): Int {
        return dataItemModelList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.e("BrowseCategory",dataItemModelList.get(0).ad_name)
        holder.name.text = dataItemModelList.get(position).ad_name
        holder.price.text = "Rs. "+dataItemModelList.get(position).ad_price
        holder.size.text = dataItemModelList.get(position).sell_size

        Glide.with(context).load(dataItemModelList.get(position).images.get(0))
            .into(holder.image)

        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val formatedDate = sdf.format(dataItemModelList.get(position).createdDate?.time!!)
        holder.date.text = formatedDate

        holder.itemView.setOnClickListener(View.OnClickListener {
            mClickListener.onItemClick(position)
        })


    }

    fun updateList(temp: MutableList<DataItemModel>) {
        dataItemModelList = temp
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val name = itemView.findViewById<TextView>(R.id.tv_name_browse)
        val price = itemView.findViewById<TextView>(R.id.tv_price_browse)
        val size = itemView.findViewById<TextView>(R.id.tv_size_browse)
        val date = itemView.findViewById<TextView>(R.id.tv_date_browse)
        val image = itemView.findViewById<ImageView>(R.id.imageView_browse)
    }
    interface ItemClickListener{
        fun onItemClick(positon: Int)
    }



}