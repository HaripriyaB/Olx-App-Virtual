package com.haripriya.olxapplication.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextSwitcher
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.haripriya.olxapplication.BaseFragment
import com.haripriya.olxapplication.R
import com.haripriya.olxapplication.models.CategoriesModel
import com.haripriya.olxapplication.ui.home.adapter.CategoryAdapter
import com.haripriya.olxapplication.utilities.Constants
import com.haripriya.olxapplication.utilities.SharedPref
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment(), CategoryAdapter.ItemClickListener {

    private var categoriesAdapter: CategoryAdapter? = null
    val db = FirebaseFirestore.getInstance()
    private var categoriesModel: MutableList<CategoriesModel> = ArrayList()
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_home, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        showProgressBar()
        tvCityName.text = SharedPref(requireActivity()).getString(Constants.CITY_NAME)
        db.collection("Categories").get().addOnSuccessListener {
            hideProgressBar()
            categoriesModel = it.toObjects(CategoriesModel::class.java)
            setAdapter()
        }
        edsearch.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                var temp: MutableList<CategoriesModel> = ArrayList()
                for(data in categoriesModel){
                    if(data.key.contains(s.toString().capitalize()) ||
                            data.key.contains(s.toString())){
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
        rv_categories.layoutManager = GridLayoutManager(context,3)
        categoriesAdapter = CategoryAdapter(categoriesModel, this)
        rv_categories.adapter = categoriesAdapter
    }

    override fun onItemClick(positon: Int) {
        val bundle = Bundle()
        bundle.putString(Constants.KEY,categoriesModel.get(positon).key)
        findNavController().navigate(R.id.action_home_to_browse_categories,bundle)

    }

}
