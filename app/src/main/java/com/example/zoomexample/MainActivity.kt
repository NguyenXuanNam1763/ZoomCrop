package com.example.zoomexample

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.zoomexample.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.*
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val IMAGE_MAX_SIZE = 2048
    private val mOutputFormat = CompressFormat.JPEG

    private var mImagePath: String? = null
    private var mSaveUri: Uri? = null
    private var mImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityMainBinding.inflate(layoutInflater)
        this.setContentView(this.binding.root)

//        val icon = BitmapFactory.decodeResource(
//            resources, R.drawable.anh_1
//        )
//
//        this.binding.ivZoom.setImageBitmap(icon)


        this.binding.ivPhoto.setImageBoundsListener(IGetImageBounds { this.binding.cropOverlay.imageBounds })

        val bitmap =
            getBitmap(Uri.fromFile(File("/storage/emulated/0/Pictures/Screenshots/test2.png")))

        this.mImagePath = File(getExternalFilesDir(null), "test.jpg").path
        mSaveUri = Utils.getImageUri(mImagePath)

        val drawable = BitmapDrawable(resources, bitmap)
        val minScale = this.binding.ivPhoto.setMinimumScaleToFit(drawable)
        this.binding.ivPhoto.maximumScale = minScale * 3
        this.binding.ivPhoto.mediumScale = minScale * 2
        this.binding.ivPhoto.scale = minScale
        this.binding.ivPhoto.setImageDrawable(drawable)

        //Initialize the MoveResize text
//        val lp = this.binding.cropOverlay.layoutParams as RelativeLayout.LayoutParams
//        lp.setMargins(0, Edge.BOTTOM.coordinate.roundToInt() + 20, 0, 0)
//        this.binding.cropOverlay.layoutParams = lp
        this.binding.btnDone.setOnClickListener {
            saveUploadCroppedImage()
        }


    }

    private fun getBitmap(uri: Uri): Bitmap? {
        var `in`: InputStream? = null
        var returnedBitmap: Bitmap? = null
        try {
            `in` = contentResolver.openInputStream(uri)
            //Decode image size
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            BitmapFactory.decodeStream(`in`, null, o)
            `in`?.close()
            var scale = 1
            if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
                scale = Math.pow(
                    2.0, Math.round(
                        Math.log(
                            IMAGE_MAX_SIZE / Math.max(o.outHeight, o.outWidth)
                                .toDouble()
                        ) / Math.log(0.5)
                    ).toDouble()
                ).toInt()
            }
            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            `in` = contentResolver.openInputStream(uri)
            var bitmap = BitmapFactory.decodeStream(`in`, null, o2)
            `in`?.close()

            //First check
            val ei = ExifInterface(uri.path!!)
            val orientation =
                ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> {
                    returnedBitmap = rotateImage(bitmap!!, 90f)
                    //Free up the memory
                    bitmap!!.recycle()
                    bitmap = null
                }
                ExifInterface.ORIENTATION_ROTATE_180 -> {
                    returnedBitmap = rotateImage(bitmap!!, 180f)
                    //Free up the memory
                    bitmap!!.recycle()
                    bitmap = null
                }
                ExifInterface.ORIENTATION_ROTATE_270 -> {
                    returnedBitmap = rotateImage(bitmap!!, 270f)
                    //Free up the memory
                    bitmap!!.recycle()
                    bitmap = null
                }
                else -> returnedBitmap = bitmap
            }
            return returnedBitmap
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    private fun saveOutput(): Boolean {
        val croppedImage: Bitmap = this.binding.ivPhoto.croppedImage
        if (mSaveUri != null) {
            var outputStream: OutputStream? = null
            try {
                outputStream = contentResolver.openOutputStream(mSaveUri!!)
                if (outputStream != null) {
                    croppedImage.compress(mOutputFormat, 90, outputStream)
                }
            } catch (ex: IOException) {
                ex.printStackTrace()
                return false
            } finally {
                closeSilently(outputStream)
            }
        } else {

            return false
        }
        croppedImage.recycle()
        return true
    }

    private fun rotateImage(source: Bitmap, angle: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

    fun saveUploadCroppedImage() {
        val saved: Boolean = saveOutput()
        if (saved) {
            Log.i("namnx", "saveUploadCroppedImage: $mImagePath")
            //USUALLY Upload image to server here
//            val intent = Intent()
//            intent.putExtra("image-path", mImagePath)
//            setResult(RESULT_OK, intent)
//            finish()
        } else {
            Toast.makeText(this, "Unable to save Image into your device.", Toast.LENGTH_LONG)
                .show()
        }
    }

    fun closeSilently(c: Closeable?) {
        if (c == null) return
        try {
            c.close()
        } catch (t: Throwable) {
            // do nothing
        }
    }

}