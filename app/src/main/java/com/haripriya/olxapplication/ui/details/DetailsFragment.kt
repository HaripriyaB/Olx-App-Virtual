package com.haripriya.olxapplication.ui.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.FirebaseFirestore
import com.haripriya.olxapplication.BaseFragment
import com.haripriya.olxapplication.R
import com.haripriya.olxapplication.models.DataItemModel
import com.haripriya.olxapplication.ui.PreviewImageActivity
import com.haripriya.olxapplication.ui.details.adapter.DetailsImageAdapter
import com.haripriya.olxapplication.utilities.Constants
import kotlinx.android.synthetic.main.fragment_details.*
import java.text.SimpleDateFormat
import java.util.*

class DetailsFragment : BaseFragment(), DetailsImageAdapter.OnItemClick {

    private lateinit var dataItemModel:DataItemModel
    val db = FirebaseFirestore.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root=inflater.inflate(R.layout.fragment_details,container,false)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //getitemdetails

        showProgressBar()
        db.collection(arguments?.getString(Constants.KEY)!!)
            .document(arguments?.getString(Constants.DOCUMENT_ID)!!)
            .get().addOnSuccessListener {

                hideProgressBar()
                dataItemModel = it.toObject(DataItemModel::class.java)!!

                //data set
                tvname.text = dataItemModel.name
                tvsize.text = dataItemModel.size
                year.text = dataItemModel.year
                author_name.text = dataItemModel.authName
                desc.text = dataItemModel.description
                phno.text = dataItemModel.phone
                tv_title.text = dataItemModel.adTitle
                tvprice.text = "Rs. "+dataItemModel.price
                tvdate.text = dataItemModel.year

                val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
                tvdate.text = dateFormat.format(dataItemModel.createdDate)


                //adapter set
                val detailImageAdapter = DetailsImageAdapter(requireContext(),dataItemModel.images,this)
                viewPager.adapter = detailImageAdapter
                viewPager.offscreenPageLimit =1

            }

        //clicklisteners
        call.setOnClickListener(View.OnClickListener {

            val dailIntent = Intent(Intent.ACTION_DIAL)
            dailIntent.data = Uri.parse("tel:"+dataItemModel.phone)
            startActivity(dailIntent)

        })


    }

    override fun onClick(position: Int) {
        startActivity(Intent(activity,PreviewImageActivity::class.java)
            .putExtra("imageUrl",dataItemModel.images.get(position)))
    }

}