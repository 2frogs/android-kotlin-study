package com.sample.sample_kotlin_android

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.sample.sample_kotlin_android.camera.CallCameraActivity
import com.sample.sample_kotlin_android.findword.FindWordMainActivity
import com.sample.sample_kotlin_android.util.MPermissionsActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onBtnClick(view : View) {
        when(view.id) {
            R.id.btn_findWord -> {
                Toast.makeText(this, "go to findword", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, FindWordMainActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_callCamera -> {
                Toast.makeText(this, "go to callcamera", Toast.LENGTH_SHORT).show()
                val intent = Intent(this,  CallCameraActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_ocr -> {
                Toast.makeText(this, "go to ocr", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, CallCameraActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }


}
