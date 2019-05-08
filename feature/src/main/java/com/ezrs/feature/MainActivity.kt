package com.ezrs.feature

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import java.util.*


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

        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkStateReceiver, filter)

//        val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
//            override fun onAvailable(network: Network) {
//
//            }
//        })

    }

    var networkStateReceiver: BroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)

            if (!noConnectivity) {
                onConnectionFound()
            } else {
                onConnectionLost()
            }
        }
    }


    fun onConnectionLost() {
    }

    fun onConnectionFound() {
        tasks.forEach { v -> v.execute() }
    }

    companion object {
        val tasks = ArrayList<AsyncTask<Void, Void, Any>>()
    }
}


