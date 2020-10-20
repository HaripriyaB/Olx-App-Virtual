package com.haripriya.olxapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.haripriya.olxapplication.BaseFragment
import com.haripriya.olxapplication.R

class NotificationsFragment : BaseFragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_notifications, container,false)
        return root
    }

}