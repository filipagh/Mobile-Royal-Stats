package com.ezrs.feature

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import java.util.*


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


    }
}


