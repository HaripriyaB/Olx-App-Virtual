package com.haripriya.olxapplication.ui.myads

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.haripriya.olxapplication.BaseFragment
import com.haripriya.olxapplication.R
import com.haripriya.olxapplication.models.DataItemModel
import com.haripriya.olxapplication.ui.myads.adapter.MyAdsAdapter
import com.haripriya.olxapplication.utilities.Constants
import com.haripriya.olxapplication.utilities.SharedPref
import kotlinx.android.synthetic.main.fragment_myads.*

class MyAdsFragment : BaseFragment(), MyAdsAdapter.ItemClickListener {
    private val documentDataList: MutableList<DataItemModel> = ArrayList()
    private lateinit var dataItemModel: MutableList<DataItemModel>
    val db=FirebaseFirestore.getInstance()
    var myAdsAdapter : MyAdsAdapter?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root=inflater.inflate(R.layout.fragment_myads,container,false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rv_myads.layoutManager=LinearLayoutManager(context)
        //getmyads
        showProgressBar()
        db.collection(Constants.CATEGORIES).get().addOnSuccessListener {
            for(i in it.documents){
                //get data from particular category
                var key = i.getString(Constants.KEY)!!
                db.collection(key)
                    .whereEqualTo("user_id",SharedPref(requireActivity()).getString(Constants.USER_ID))
                    .get().addOnSuccessListener {
                        hideProgressBar()
                        dataItemModel = it.toObjects(DataItemModel::class.java)
                        documentDataList.addAll(dataItemModel)
                        setAdapter()
                    }

            }
        }
        hideProgressBar()

    }

    private fun setAdapter() {
        myAdsAdapter =
            MyAdsAdapter(documentDataList, this)
        if (rv_myads != null)
            rv_myads.adapter = myAdsAdapter
    }

    override fun onItemClick(positon: Int) {

        var bundle = Bundle()
        bundle.putString(Constants.KEY,documentDataList.get(positon).type)
        bundle.putString(Constants.Id,documentDataList.get(positon).id)
        findNavController().navigate(R.id.action_myads_to_details,bundle)

    }


}