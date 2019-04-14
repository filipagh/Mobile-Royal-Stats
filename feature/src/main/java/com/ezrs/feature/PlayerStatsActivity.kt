package com.ezrs.feature

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.ezrs.feature.MyService.Companion.API_BASE_PATH
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.text.TextRecognizer
import io.swagger.client.api.PlayersApi
import io.swagger.client.api.UsersApi
import io.swagger.client.model.Tag
import io.swagger.client.model.UserStat
import io.swagger.client.model.UserView
import android.support.v4.content.LocalBroadcastManager
import android.text.Editable


/**
 * A login screen that offers login via email/password.
 */
class PlayerStatsActivity : Activity() {

//    var mAuthTask: PlayerStatsTask? = null
    lateinit var playerStats: UserStat
    lateinit var editID: EditText
    lateinit var buttonFindId: Button
    val PICK_PHOTO_CODE = 1046
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.player_stats_utils)

        editID = findViewById(R.id.editID) as EditText
        buttonFindId = findViewById(R.id.buttonFindId) as Button
        buttonFindId.setOnClickListener {

            val intent = Intent("PlayerStatsTag")
            // You can also include some extra data.

            intent.putExtra("Tag", editID.text.toString())
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)

            finish()
        }


        var int = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if (int.resolveActivity(getPackageManager()) != null) {
            // Bring up gallery to select a photo
            startActivityForResult(int, PICK_PHOTO_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (data != null) {
            val photoUri = data.getData();
            // Do something with the photo based on Uri
            val selectedImage: Bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri)

            //Create the TextRecognizer
            val textRecognizer = TextRecognizer.Builder(getApplicationContext()).build()


            //Set the TextRecognizer's Processor.
            textRecognizer.setProcessor(object : Detector.Processor<TextBlock> {
                override fun release() {}

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
//                    Log.d("OCR",stringBuilder.toString())
                }
            })


            var fb = Frame.Builder()
            fb.setBitmap(selectedImage)
            val frame = fb.build()
            var a = textRecognizer.detect(frame)
            var vysledok = arrayListOf<String>()
            for (i in 0 until a.size()) {
                try {
                    vysledok.add(a[i].value)
                } catch (e: Exception) {
                    //java.lang.IllegalStateException neviem preco toto sa deje
                }
            }

            // TODO nejaky parser
            val tag = vysledok[0]

            editID.text.clear()
            editID.text.insert(0, tag)
        }
    }
}