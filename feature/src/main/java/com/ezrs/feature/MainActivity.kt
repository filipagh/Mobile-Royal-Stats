package com.ezrs.feature

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.provider.ContactsContract

import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.SurfaceView
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.text.TextRecognizer


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // permison na zobrazovanie nad aplikaciami
        val REQUEST_CODE = 100
//        val intent_permision = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()))
//        startActivityForResult(intent_permision, REQUEST_CODE)



        //bublinka
        var intent = Intent(this, MyService::class.java)
        var bublinkaService = startService(intent)





/////////////////////////// TOTO NEHAJ
/*



//
//        mCameraView = findViewById(R.id.surfaceView)
//

//        val v1 = window.decorView.rootView
//
//        val bitmap = Bitmap.createBitmap(1080, 1900, Bitmap.Config.ARGB_8888)
//        val canvas = Canvas(bitmap)
//        val bgDrawable = v1.background


//        this.window.decorView.rootView.


//        var imageView: ImageView = findViewById(R.id.imageView)
//        imageView.setImageBitmap(bitmap)
//        startCameraSource()

    }

//    fun getBitmapFromView(view: View, activity: Activity, callback: (Bitmap) -> Unit) {
//        activity.window?.let { window ->
//            val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
//            val locationOfViewInWindow = IntArray(2)
//            view.getLocationInWindow(locationOfViewInWindow)
//            try {
//                PixelCopy.request(window, Rect(locationOfViewInWindow[0], locationOfViewInWindow[1], locationOfViewInWindow[0] + view.width, locationOfViewInWindow[1] + view.height), bitmap, { copyResult ->
//                    if (copyResult == PixelCopy.SUCCESS) {
//                        callback(bitmap)
//                    }
//                    // possible to handle other result codes ...
//                }, Handler())
//            } catch (e: IllegalArgumentException) {
//                // PixelCopy may throw IllegalArgumentException, make sure to handle it
//                e.printStackTrace()
//            }
//        }
//    }


    fun startCameraSource() {

        //Create the TextRecognizer
        val textRecognizer = TextRecognizer.Builder(getApplicationContext()).build()


            //Set the TextRecognizer's Processor.
            textRecognizer.setProcessor(object : Detector.Processor<TextBlock> {
                override fun release() {}
                /*
                 * Detect all the text from camera using TextBlock and the values into a stringBuilder
                 * which will then be set to the textView.
                 */
                override fun receiveDetections(detections: Detector.Detections<TextBlock>) {
                    val items = detections.detectedItems
                    var stringBuilder = StringBuffer("")


                    if (items.size() !== 0) {

                        for (i in 0 until items.size()) {
                            val item = items.valueAt(i)
                            stringBuilder.append(item.value)
                            stringBuilder.append("\n")
                        }

                    }
                    Log.d("OCR",stringBuilder.toString())
                }
            })




//            var fb = Frame.Builder()
//            fb.
//            textRecognizer.detect()

*/
        }





}


