package com.ezrs.feature

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    //    @RequiresApi(24)
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
    }

    companion object {
        val TASK_PREFERENCE = "TASKS"
    }
}


