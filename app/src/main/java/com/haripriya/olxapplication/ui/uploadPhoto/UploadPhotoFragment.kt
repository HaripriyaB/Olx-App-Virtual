package com.haripriya.olxapplication.ui.uploadPhoto

import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.haripriya.olxapplication.BaseFragment
import com.haripriya.olxapplication.MainActivity
import com.haripriya.olxapplication.R
import com.haripriya.olxapplication.ui.PreviewImageActivity
import com.haripriya.olxapplication.ui.uploadPhoto.adapter.UploadPhotoAdapter
import com.haripriya.olxapplication.utilities.Constants
import com.haripriya.olxapplication.utilities.OnActivityResultData
import com.haripriya.olxapplication.utilities.SharedPref
import kotlinx.android.synthetic.main.fragment_sell.*
import kotlinx.android.synthetic.main.fragment_upload_photo.*
import net.alhazmy13.mediapicker.Image.ImagePicker
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class UploadPhotoFragment : BaseFragment(), UploadPhotoAdapter.ItemClickListener {

    private val imageUriList: ArrayList<String> = ArrayList()
    private var count  = 0
    private var uploadTask : UploadTask? = null
    private var imagesAdapter : UploadPhotoAdapter? = null
    private var outputfileUri: String? = null
    private var selectedImageArrayList : ArrayList<String> = ArrayList()
    internal var dialog: BottomSheetDialog?=null
    internal var selectedImage: File?=null
    internal var TAG = UploadPhotoFragment::class.java.simpleName
    val db = FirebaseFirestore.getInstance()
    internal lateinit var storageRef : StorageReference
    internal lateinit var storage : FirebaseStorage
    internal lateinit var imageRef : StorageReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_upload_photo,container,false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rv_photo.layoutManager = GridLayoutManager(context,3)
        storage = FirebaseStorage.getInstance()
        storageRef = storage.getReference()

        //click listeners
        ivchoosephoto.setOnClickListener{
           showBottomSheetDialog()
        }
        buttonPreview.setOnClickListener{
            if(selectedImage!=null){
                startActivity(Intent(activity,PreviewImageActivity::class.java)
                    .putExtra("imageUri",outputfileUri))
            }
        }
        buttonUpload.setOnClickListener{
            if(selectedImage == null || !selectedImage!!.exists()){
                Toast.makeText(requireActivity(),"Please select a photo",Toast.LENGTH_SHORT).show()
            }else{
                saveFileInFirebaseStorage()
            }
        }

        (activity as MainActivity).getOnActivityResultData(object : OnActivityResultData{
            override fun resultData(bundle: Bundle) {
            linearlayout_choosephoto.visibility = View.GONE
                rv_photo.visibility = View.VISIBLE
                val paths=bundle.getStringArrayList(Constants.IMAGE_PATH)
                selectedImage = File(paths!![0])
                outputfileUri = paths[0]
                selectedImageArrayList.add(paths[0])
                setAdapter()
            }
        })
    }

    private fun saveFileInFirebaseStorage() {
        for(i in 0..selectedImageArrayList.size-1){
            val file = File(selectedImageArrayList[i])
            imageRef = storageRef.child("images/${file.name}")
            uploadTask = imageRef.putFile(Uri.fromFile(file))
            uploadTask!!.addOnSuccessListener ( object : OnSuccessListener<UploadTask.TaskSnapshot>{
                override fun onSuccess(p0: UploadTask.TaskSnapshot?) {
                    imageRef.downloadUrl.addOnSuccessListener {
                        count++
                        val url  = p0.toString()
                        imageUriList.add(url)
                        if(count == selectedImageArrayList.size){
                            //postAd
                            showProgressBar()
                            val documentid=db.collection(
                                arguments?.getString(Constants.KEY)!!).document().id
                            val documentData = hashMapOf(
                                Constants.AD_NAME to arguments?.getString(Constants.AD_NAME),
                                Constants.AD_AUTHOR_NAME to arguments?.getString(Constants.AD_AUTHOR_NAME),
                                Constants.YEAR_SELL to arguments?.getString(Constants.YEAR_SELL),
                                Constants.SELL_SIZE to arguments?.getString(Constants.SELL_SIZE),
                                Constants.AD_TITLE to arguments?.getString(Constants.AD_TITLE),
                                Constants.AD_DESC to arguments?.getString(Constants.AD_DESC),
                                Constants.AD_PRICE to arguments?.getString(Constants.AD_PRICE),
                                Constants.AD_PHNO to arguments?.getString(Constants.AD_PHNO),
                                Constants.TYPE to arguments?.getString(Constants.KEY),
                                Constants.Id to documentid,
                                Constants.USER_ID to SharedPref(activity!!).getString(Constants.USER_ID),
                                Constants.CREATED_DATE to Date(),
                                "images" to imageUriList
                            )
                            db.collection(arguments?.getString(Constants.KEY)!!)
                                .add(documentData)
                                .addOnSuccessListener {
                                    var id : String = it.id
                                    val docData = mapOf(
                                        Constants.Id to id
                                    )
                                    db.collection(arguments?.getString(Constants.KEY)!!)
                                        .document(id)
                                        .update(docData).addOnSuccessListener {
                                            hideProgressBar()
                                            Toast.makeText(activity!!,"Ad posted Sucessfully",Toast.LENGTH_SHORT).show()
                                        }

                                }
                        }
                    }
                }

            } )
        }
    }

    private fun showBottomSheetDialog() {
        val layoutInflator = requireActivity().getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflator.inflate(R.layout.bottomsheet_dialog,null)
        dialog = BottomSheetDialog(requireActivity())
        dialog!!.setContentView(view)
        dialog!!.window?.findViewById<View>(R.id.design_bottom_sheet)
            ?.setBackgroundColor(resources.getColor(android.R.color.transparent))

        var textViewGallery = dialog!!.findViewById<TextView>(R.id.tv_photogallery)
        var textViewCamera = dialog?.findViewById<TextView>(R.id.tv_camera)
        var textViewCancel = dialog?.findViewById<TextView>(R.id.tv_cancel)

        textViewGallery?.setOnClickListener{
            dialog?.dismiss()
            ChooseImage(ImagePicker.Mode.GALLERY)
        }

        textViewCamera?.setOnClickListener{
            dialog?.dismiss()
            ChooseImage(ImagePicker.Mode.CAMERA)
        }

        textViewCancel?.setOnClickListener{
            dialog?.dismiss()
        }

        dialog?.show()
        val lp = WindowManager.LayoutParams()
        val window = dialog?.window
        lp.copyFrom(window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        window.attributes = lp

    }

    private fun setAdapter() {
        if(imagesAdapter!=null){
            imagesAdapter!!.customNotify(selectedImageArrayList)
        }else{
            imagesAdapter = UploadPhotoAdapter(requireActivity(),selectedImageArrayList,this)
            rv_photo.adapter = imagesAdapter
        }
    }

    private fun ChooseImage(mode:ImagePicker.Mode)
    {
        ImagePicker.Builder(requireActivity())
            .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
            .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
            .directory(ImagePicker.Directory.DEFAULT)
            .extension(ImagePicker.Extension.PNG)
            .scale(600, 600)
            .allowMultipleImages(false)
            .enableDebuggingMode(true)
            .build()
    }

    override fun onItemClick() {
        showBottomSheetDialog()
    }

}