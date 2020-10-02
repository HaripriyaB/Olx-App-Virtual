package com.haripriya.olxapplication.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.haripriya.olxapplication.BaseFragment
import com.haripriya.olxapplication.R
import com.haripriya.olxapplication.utilities.Constants
import com.haripriya.olxapplication.utilities.SharedPref
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_settings,container,false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        full_name.setText(SharedPref(requireActivity()).getString(Constants.USER_NAME))
        email_id.setText(SharedPref(requireActivity()).getString(Constants.USER_EMAIL))
        phone_number.setText(SharedPref(requireActivity()).getString(Constants.USER_PHONE))

        save_btn.setOnClickListener(View.OnClickListener {

            if(full_name.text.toString().isEmpty()){
                full_name.setError("Full Name can't be blank")
            }else if(email_id.text.toString().isEmpty()){
                email_id.setError("Email can't be blank")
            }else if(phone_number.text.toString().isEmpty()){
                phone_number.setError("Phone number can't be blank")
            }else{
                SharedPref(requireActivity()).setString(Constants.USER_EMAIL,email_id.text.toString())
                SharedPref(requireActivity()).setString(Constants.USER_NAME,full_name.text.toString())
                SharedPref(requireActivity()).setString(Constants.USER_PHONE,phone_number.text.toString())
                Toast.makeText(context,"Saved Successfully",Toast.LENGTH_SHORT).show()
//                fragmentManager?.popBackStack()
                findNavController().navigate(R.id.action_settings_to_profile)

            }

        })


    }


}