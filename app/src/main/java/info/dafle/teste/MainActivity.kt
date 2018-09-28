package info.dafle.teste

import android.Manifest
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.tarek360.instacapture.Instacapture
import com.tarek360.instacapture.listener.SimpleScreenCapturingListener
import kotlinx.android.synthetic.main.activity_main.*
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener
import android.Manifest.permission
import android.webkit.PermissionRequest
import com.karumi.dexter.Dexter
import android.content.Intent
import android.net.Uri
import android.R.attr.bitmap
import android.app.Activity
import android.graphics.Canvas
import android.graphics.Color
import android.provider.MediaStore
import android.view.View


class MainActivity : AppCompatActivity() {

    lateinit var mActivity: Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        mActivity = this

        bt_print.setOnClickListener {

            Dexter.withActivity(mActivity)
                    .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(object : PermissionListener {
                        override fun onPermissionRationaleShouldBeShown(permission: com.karumi.dexter.listener.PermissionRequest?, token: PermissionToken?) {


                            take()
                        }

                        override fun onPermissionGranted(response: PermissionGrantedResponse) {/* ... */

                            take()
                        }

                        override fun onPermissionDenied(response: PermissionDeniedResponse) {/* ... */
                        }
                    }).check()

        }
    }

    fun take() {

        val bitmapPath = MediaStore.Images.Media.insertImage(contentResolver, getBitmapFromView(cc), "title", null)
        val bitmapUri = Uri.parse(bitmapPath)
        shareImageUri(bitmapUri)
    }

    fun getBitmapFromView(view: View): Bitmap {
        //Define a bitmap with the same size as the view
        val returnedBitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        //Bind a canvas to it
        val canvas = Canvas(returnedBitmap)
        //Get the view's background
        val bgDrawable = view.background
        if (bgDrawable != null)
        //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas)
        else
        //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE)
        // draw the view on the canvas
        view.draw(canvas)
        //return the bitmap
        return returnedBitmap
    }

    /**
     * Shares the PNG image from Uri.
     * @param uri Uri of image to share.
     */
    private fun shareImageUri(uri: Uri) {
        val intent = Intent(android.content.Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.type = "image/png"
        startActivity(intent)
    }
}