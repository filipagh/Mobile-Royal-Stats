package com.ezrs.feature


import android.app.ActivityManager

import android.graphics.Color;

import android.support.v4.content.ContextCompat;

import android.view.Gravity;


import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.os.IBinder
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.ButtonBarLayout
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.bsk.floatingbubblelib.FloatingBubbleService
import com.bsk.floatingbubblelib.FloatingBubbleConfig
import com.bsk.floatingbubblelib.FloatingBubblePhysics
import com.bsk.floatingbubblelib.FloatingBubbleTouch
import com.ezrs.feature.R.id.textik2
import io.swagger.client.api.*
import io.swagger.client.model.Tag
import org.json.JSONArray
import org.json.JSONObject


class MyService : FloatingBubbleService(),Tab1.OnFragmentInteractionListener {
    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    // Service() {


    lateinit var root : View
    lateinit var bubbleUtils: BubbleUtils

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.d("aa","tu som")

        return super.onStartCommand(intent, flags, startId)

    }


      override fun getConfig(): FloatingBubbleConfig {
        root = getInflater().inflate(R.layout.test1, null)
        val gombik = Button(context)

        var button: Button? = null
        button = root.findViewById(R.id.button) as Button



          var viewp : ViewPager = root.findViewById(R.id.pager) as ViewPager
          viewp.adapter =CustomPagerAdapter(this)

        button.setOnClickListener {
            klik(root)
        }
        bubbleUtils = BubbleUtils(context,root)

          // tuto treba dako mu vysvetlit nech zobere nase BubbleUtil a nie jeho default ale zatial nwm ako
//        this.setTouchListener() {
//            super.getTouchListener()
//
//        }


        return FloatingBubbleConfig.Builder()

                .bubbleIcon(ContextCompat.getDrawable(context, R.drawable.web_icon))

                .removeBubbleIcon(ContextCompat.getDrawable(context, com.bsk.floatingbubblelib.R.drawable.close_default_icon))

                .bubbleIconDp(54)

                .expandableView(root)

                .removeBubbleIconDp(54)

                .paddingDp(4)

                .borderRadiusDp(0)

                .physicsEnabled(true)

                .expandableColor(Color.WHITE)

                .triangleColor(10)

                .gravity(Gravity.LEFT)

                .build()
    }

    /////////////////////////////




    fun klik(vstup: View) {




       // bubbleUtils.onTap(true)


//        var a = getRecentTasks(5,0,0)
//
//        val r = 54

//
//        val textik = vstup.findViewById(R.id.textik1) as TextView
//////        textik.text = "HURA2"
//        val client = PlayersApi()
//        client.basePath = "http://192.168.0.187:8080/Mobile_Royal_Stats_Server_war_exploded/rest"
//        var tag = Tag()
////        tag.tag = "88UPPPVR8"
//        val nieco = client.info(tag)
//        val task = AsyncTask() {
//
//        }
//        textik.text = client.sayHello()
        val operation = LongOperation()
        operation.execute("")
    }


    private inner class LongOperation : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg params: String): String {
            val client = UsersApi()
            client.basePath = "http://192.168.1.20:8080/Mobile_Royal_Stats_Server_war_exploded/rest"
            return client.sayHello()
        }

        override fun onPostExecute(result: String) {
            val textik : TextView = root.findViewById(textik2) as TextView
            textik.text = result
//            txt.text = "Executed" // txt.setText(result);
            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
        }

        override fun onPreExecute() {}

        override fun onProgressUpdate(vararg values: Void) {}
    }


}

