package com.haripriya.olxapplication.ui.uploadPhoto.adapter

import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.haripriya.olxapplication.R

class UploadPhotoAdapter(internal var activity:Activity,
internal var imagedArrayList : ArrayList<String>, internal var itemClickListener: ItemClickListener) : RecyclerView.Adapter<UploadPhotoAdapter.ViewHolder>() {
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val viewHolder = LayoutInflater.from(parent.context).inflate(R.layout.adapter_upload_image,parent, false)
        return ViewHolder(viewHolder)
    }

    override fun getItemCount(): Int {
        return imagedArrayList.size+1
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(position<imagedArrayList.size){
            val bitmap = BitmapFactory.decodeFile(imagedArrayList[position])
            holder.image.setImageBitmap(bitmap)
        }
        holder.itemView.setOnClickListener(View.OnClickListener{
            if(position == imagedArrayList.size){
                itemClickListener.onItemClick()
            }
        })

    }

    fun updateList(temp: ArrayList<String>) {
        imagedArrayList = temp
        notifyDataSetChanged()
    }

    fun customNotify(selectedImagesArrayList:java.util.ArrayList<String>) {
        this.imagedArrayList = selectedImagesArrayList
        notifyDataSetChanged()

    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val image = itemView.findViewById<ImageView>(R.id.iv_addphoto)
    }
    interface ItemClickListener{
        fun onItemClick()
    }

}
