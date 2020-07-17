package com.haripriya.olxapplication.ui.sell

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.haripriya.olxapplication.BaseFragment
import com.haripriya.olxapplication.R
import com.haripriya.olxapplication.models.CategoriesModel
import com.haripriya.olxapplication.ui.sell.adapter.SellAdapter
import com.haripriya.olxapplication.utilities.Constants
import com.haripriya.olxapplication.utilities.SharedPref
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_sell.*

class SellFragment : BaseFragment(), SellAdapter.ItemClickListener {

    val db = FirebaseFirestore.getInstance()
    private lateinit var categoriesModel: MutableList<CategoriesModel>

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_sell, container, false)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        showProgressBar()
        db.collection("Categories").get().addOnSuccessListener {
            hideProgressBar()
            categoriesModel = it.toObjects(CategoriesModel::class.java)
            setAdapter()
        }
    }

    private fun setAdapter() {
        rv_sell.layoutManager = GridLayoutManager(context,3)
        val sellAdapter = SellAdapter(categoriesModel, this)
        rv_sell.adapter = sellAdapter

    }

    override fun onItemClick(positon: Int) {
        val bundle = Bundle()
        bundle.putString("key",categoriesModel.get(positon).key)
        findNavController().navigate(R.id.action_sell_to_include_details,bundle)
    }
}
