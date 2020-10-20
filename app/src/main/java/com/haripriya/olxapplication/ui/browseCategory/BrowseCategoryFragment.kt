package com.haripriya.olxapplication.ui.browseCategory

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.haripriya.olxapplication.BaseFragment
import com.haripriya.olxapplication.R
import com.haripriya.olxapplication.models.CategoriesModel
import com.haripriya.olxapplication.models.DataItemModel
import com.haripriya.olxapplication.ui.browseCategory.adapter.BrowseCategoryAdapter
import com.haripriya.olxapplication.utilities.Constants
import kotlinx.android.synthetic.main.fragment_browse.*
import kotlinx.android.synthetic.main.fragment_home.*

class BrowseCategoryFragment : BaseFragment(), BrowseCategoryAdapter.ItemClickListener {

    private var dataItemModel: MutableList<DataItemModel> = ArrayList()
    val db = FirebaseFirestore.getInstance()
    private var categoriesAdapter: BrowseCategoryAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_browse,container,false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        showProgressBar()
        rv_browse.layoutManager = LinearLayoutManager(context)
        db.collection(arguments?.getString(Constants.KEY)!!)
            .get().addOnSuccessListener {

                hideProgressBar()
                dataItemModel = it.toObjects(DataItemModel::class.java)
                setAdapter()
            }


        edbrowse.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                var temp: MutableList<DataItemModel> = ArrayList()
                for(data in dataItemModel){
                    if(data.ad_title.contains(s.toString().capitalize()) ||
                        data.ad_title.contains(s.toString()) ||
                        data.ad_name.contains(s.toString().capitalize()) ||
                        data.ad_name.contains(s.toString()) ||
                        data.ad_author_name.contains(s.toString().capitalize()) ||
                        data.ad_author_name.contains(s.toString()) ||
                        data.type.contains(s.toString().capitalize()) ||
                        data.type.contains(s.toString()) ||
                        data.ad_desc.contains(s.toString().capitalize()) ||
                        data.ad_desc.contains(s.toString())){
                        temp.add(data)
                    }
                }
                //update list
                categoriesAdapter?.updateList(temp)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })


    }

    private fun setAdapter() {
        var categoriesadaptor = BrowseCategoryAdapter(dataItemModel,this)
        rv_browse.adapter = categoriesadaptor
    }

    override fun onItemClick(positon: Int) {
        var bundle = Bundle()
        bundle.putString(Constants.DOCUMENT_ID,dataItemModel.get(positon).id)
        bundle.putString(Constants.KEY,dataItemModel.get(positon).type)
        findNavController().navigate(R.id.action_browse_to_details,bundle)
    }

}