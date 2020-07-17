package com.haripriya.olxapplication

import android.app.Activity
import android.content.Intent
import android.media.Image
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.haripriya.olxapplication.utilities.Constants
import com.haripriya.olxapplication.utilities.OnActivityResultData
import net.alhazmy13.mediapicker.Image.ImagePicker

class MainActivity : AppCompatActivity() {

    lateinit var onActivityResultData : OnActivityResultData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_sell, R.id.navigation_notifications))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        System.exit(0)
    }

    fun getOnActivityResultData(onActivityResultData: OnActivityResultData){
        this.onActivityResultData = onActivityResultData
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            val mpaths=data?.getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_PATH)
            val bundle = Bundle()
            bundle.putStringArrayList(Constants.IMAGE_PATH,mpaths)
            onActivityResultData.resultData(bundle)

        }
    }
}
