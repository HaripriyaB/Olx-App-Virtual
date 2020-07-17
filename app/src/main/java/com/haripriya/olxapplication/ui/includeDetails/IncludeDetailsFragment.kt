package com.haripriya.olxapplication.ui.includeDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.haripriya.olxapplication.BaseFragment
import com.haripriya.olxapplication.R
import com.haripriya.olxapplication.utilities.Constants
import kotlinx.android.synthetic.main.adapter_categories.*
import kotlinx.android.synthetic.main.fragment__include_details.*

class IncludeDetailsFragment : BaseFragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment__include_details, container,false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tv_next.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tv_next ->{
                if(sellname.text?.isEmpty()!!){
                    sellname.setError("Enter valid number")
                }
                else if(sellauthor.text?.isEmpty()!!){
                    sellauthor.setError("Enter valid author name")
                }
                else if(sellyear.text?.isEmpty()!!){
                    sellyear.setError("Enter valid selling year")
                }
                else if(edtitle.text?.isEmpty()!!){
                    edtitle.setError("Enter title")
                }
                else if(sellprice.text?.isEmpty()!!){
                    sellprice.setError("Enter price")
                }
                else if(phone.text?.isEmpty()!!){
                    phone.setError("Enter phone number")
                }
                else{
                    val bundle = Bundle()
                    bundle.putString(Constants.AD_NAME,sellname.text.toString())
                    bundle.putString(Constants.AD_AUTHOR_NAME,sellauthor.text.toString())
                    bundle.putString(Constants.YEAR_SELL,sellyear.text.toString())
                    bundle.putString(Constants.SELL_SIZE,sellsize.text.toString())
                    bundle.putString(Constants.AD_TITLE,edtitle.text.toString())
                    bundle.putString(Constants.AD_DESC,eddescribe.text.toString())
                    bundle.putString(Constants.AD_PRICE,sellprice.text.toString())
                    bundle.putString(Constants.AD_PHNO,phone.text.toString())
                    bundle.putString(Constants.KEY,arguments?.getString(Constants.KEY))
                    findNavController().navigate(R.id.action_details_photo_upload,bundle)
                }
            }
        }
    }

}