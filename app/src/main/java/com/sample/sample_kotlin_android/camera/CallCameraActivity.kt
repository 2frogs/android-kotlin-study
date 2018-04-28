package com.sample.sample_kotlin_android.camera;

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.*
import android.app.ProgressDialog
import com.sample.sample_kotlin_android.R
import com.sample.sample_kotlin_android.util.MPermissionsActivity
import kotlinx.android.synthetic.main.camera_activity.*

class CallCameraActivity : MPermissionsActivity() {

    val CAMERA_REQUEST_CODE = 1
    val SELPIC_REQUEST_CODE = 2

    private var mProgressDialog: ProgressDialog? = null
    private var mOcrHelper: OcrHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.camera_activity)

        /*apply permission for camera and storeage*/
        requestPermission(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                  android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                  android.Manifest.permission.CAMERA), 0x0002)

        mOcrHelper = OcrHelper()
    }

    fun click(view : View) {
        when(view.id) {
            R.id.loadCamera -> {  /* 相应按钮事件, 调用系统相机 */
                val loadCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if(loadCameraIntent.resolveActivity(packageManager) != null) {
                    startActivityForResult(loadCameraIntent, CAMERA_REQUEST_CODE)
                }
            }
            R.id.selectPic -> {
                val selectPicIntent = Intent()
                selectPicIntent.type = "image/*"
                selectPicIntent.action = Intent.ACTION_GET_CONTENT
                if(selectPicIntent.resolveActivity(packageManager) != null) {
                    startActivityForResult(Intent.createChooser(selectPicIntent,
                                                "select pic"), SELPIC_REQUEST_CODE)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            CAMERA_REQUEST_CODE -> {   /* 处理相机拍摄返回结果 */
                if(resultCode == Activity.RESULT_OK && data != null) {
                    val picBitmap : Bitmap = data.extras.get("data") as Bitmap
                    imageView.setImageBitmap(picBitmap)    //显示图片
                    savePicToSdCard(picBitmap)
                    doOcr(picBitmap);   /* 识别图像字母 */
                }
            }
            SELPIC_REQUEST_CODE -> {
                if(resultCode == Activity.RESULT_OK && data != null) {
                    val uri : Uri? = data.data
                    if(uri != null) {
                        var instream : InputStream? = null
                        try {
                            instream = contentResolver.openInputStream(uri)
                            val selPicBitmap : Bitmap  = BitmapFactory.decodeStream(instream);
                            imageView.setImageBitmap(selPicBitmap)
                            doOcr(selPicBitmap); /* 识别图像字母 */
                        } catch (e : Exception) {
                            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                        } finally {
                        }
                    }
                }
            }
            else -> {
                Toast.makeText(this, "Unrecognized request code", Toast.LENGTH_SHORT).show()
            }
        }

    }

    /*
    * 保存图片到SD卡
    */
    fun savePicToSdCard(picBitmap : Bitmap?) {
        val sdStatus = Environment.getExternalStorageState()
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "SDCARD is not ready", Toast.LENGTH_SHORT).show()
            return
        }
        var fos: FileOutputStream? = null
        try {
            val fileDir = Environment.getExternalStorageDirectory().absolutePath + "/ATest/"
            val file: File = File(fileDir)
            if (!file.exists()) {
                file.mkdirs();
            }
            fos = FileOutputStream(fileDir + UUID.randomUUID() + ".jpg")
            if (picBitmap != null) {
                picBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                fos.flush()
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        } finally {
            fos?.flush()
            fos?.close()
        }
    }

    fun doOcr(bitmap : Bitmap) {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog.show(this, "Processing",
                    "Doing OCR...", true);
        }
        else {
            mProgressDialog?.show();
        }
        Thread(Runnable {
            val result = mOcrHelper?.getOcrResult(bitmap)
            runOnUiThread {
                if (result != null && result != "") {
                    textView.text = result
                }
                mProgressDialog?.dismiss()
            }
        }).start()
    }

}

