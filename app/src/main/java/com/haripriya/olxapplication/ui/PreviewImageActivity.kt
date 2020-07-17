package com.haripriya.olxapplication.ui

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.PersistableBundle
import android.view.ScaleGestureDetector
import android.view.View
import com.bumptech.glide.Glide
import com.haripriya.olxapplication.BaseActivity
import com.haripriya.olxapplication.R
import kotlinx.android.synthetic.main.activity_preview_image.*

class PreviewImageActivity : BaseActivity() {
    private var mScaleGestureDetector: ScaleGestureDetector? = null
    private var mScaleFactor = 1.0f
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_preview_image)

        val imagePath = intent.extras
        if (imagePath?.containsKey("imageUri")!!) {
            val imageUri = imagePath?.getString("imageUri")
            val imageBitmap = BitmapFactory.decodeFile(imageUri)
            imagepreview.setImageBitmap(imageBitmap)
        } else {
            val imageURL = imagePath.getString("imageUrl")
            Glide.with(this).load(imageURL)
                .into(imagepreview)
        }

        imagepreview.scaleX = mScaleFactor
        imagepreview.scaleY = mScaleFactor

        mScaleGestureDetector = ScaleGestureDetector(this, ScaleListener())

        closebtn.setOnClickListener(View.OnClickListener {

            finish()

        })

    }

    private inner class ScaleListener : ScaleGestureDetector.OnScaleGestureListener {
        override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
            mScaleFactor*=detector?.scaleFactor!!
            mScaleFactor = Math.max(0.1f,Math.min(mScaleFactor,10.0f))
            imagepreview.scaleX = mScaleFactor
            imagepreview.scaleY = mScaleFactor
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector?) {
            TODO("Not yet implemented")
        }

        override fun onScale(detector: ScaleGestureDetector?): Boolean {
            TODO("Not yet implemented")
        }

    }
}
