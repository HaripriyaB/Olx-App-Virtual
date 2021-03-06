package com.haripriya.olxapplication.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.haripriya.olxapplication.R
import com.haripriya.olxapplication.models.CategoriesModel
import com.haripriya.olxapplication.ui.home.HomeFragment
import de.hdodenhof.circleimageview.CircleImageView

class CategoryAdapter(var categoriesList:MutableList<CategoriesModel>
                      ,var itemClickListener: ItemClickListener) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val viewHolder = LayoutInflater.from(parent.context).inflate(R.layout.adapter_categories,parent, false)
        return ViewHolder(viewHolder)
    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = categoriesList.get(position).key
        Glide.with(context).load(categoriesList.get(position).image)
            .into(holder.image)
        holder.itemView.setOnClickListener(View.OnClickListener {
            itemClickListener.onItemClick(position)
        })


    }

    fun updateList(temp: MutableList<CategoriesModel>) {
        categoriesList = temp
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.tvTitle)
        val image = itemView.findViewById<CircleImageView>(R.id.ivicon)
    }
    interface ItemClickListener{
        fun onItemClick(positon: Int)
    }

}
