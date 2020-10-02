package com.haripriya.olxapplication.ui.profile

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.facebook.login.LoginManager
import com.facebook.share.Share
import com.google.firebase.auth.FirebaseAuth
import com.haripriya.olxapplication.BaseFragment
import com.haripriya.olxapplication.R
import com.haripriya.olxapplication.ui.login.LoginActivity
import com.haripriya.olxapplication.utilities.Constants
import com.haripriya.olxapplication.utilities.SharedPref
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile,container,false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        username.text = SharedPref(requireActivity()).getString(Constants.USER_NAME)
        email.text = SharedPref(requireActivity()).getString(Constants.USER_EMAIL)
        Glide.with(requireActivity())
            .load(SharedPref(requireActivity()).getString(Constants.USER_PHOTO))
            .into(imageViewUserProfile)

        //listeners
        ll_settings.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.action_profile_to_settings)
        })

        ll_logout.setOnClickListener(View.OnClickListener {
            showDialogBox()

        })

    }

    private fun showDialogBox() {
        var builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(R.string.logout)
        builder.setMessage(getString(R.string.sure_logout))
        builder.setIcon(R.drawable.ic_warning)
        builder.setPositiveButton(getString(R.string.yes)){dialog: DialogInterface?, which: Int ->

            FirebaseAuth.getInstance().signOut()
            LoginManager.getInstance().logOut()
            //clear session

            clearSession()

            startActivity(Intent(requireActivity(),LoginActivity::class.java))
            requireActivity().finish()
            dialog?.dismiss()
        }
        builder.setNegativeButton(getString(R.string.no)){dialog: DialogInterface?, which: Int ->
            dialog?.dismiss()
        }

        val alertDialog:AlertDialog= builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()

    }

    private fun clearSession() {
        SharedPref(requireActivity()).setString(Constants.USER_PHOTO,"")
        SharedPref(requireActivity()).setString(Constants.USER_EMAIL,"")
        SharedPref(requireActivity()).setString(Constants.USER_ID,"")
        SharedPref(requireActivity()).setString(Constants.USER_NAME,"")
    }

}
