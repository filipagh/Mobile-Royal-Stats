package com.ezrs.feature


import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.AsyncTask
import android.os.IBinder
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import com.bsk.floatingbubblelib.FloatingBubbleConfig
import com.bsk.floatingbubblelib.FloatingBubbleService
import io.swagger.client.api.UsersApi


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


    fun klik(v: View) {

       // bubbleUtils.onTap(true)
//        var a = getRecentTasks(5,0,0)
//        val r = 54

//        val textik = v.findViewById(R.id.textik1) as TextView
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
        //val operation = LongOperation()
        //operation.execute("")
        val dialogIntent = Intent(this, LoginActivity::class.java)
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(dialogIntent)
    }

    fun login(v: View) {
        val dialogIntent = Intent(this, LoginActivity::class.java)
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(dialogIntent)
        setState(false)
    }

    fun clan(v: View) {
        if (getSharedPreferences(LoginActivity.PREFERENCE, Activity.MODE_PRIVATE).getString(LoginActivity.APIKEY, "") == "") {
            val dialogIntent = Intent(this, LoginActivity::class.java)
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(dialogIntent)
            setState(false)
        } else {
            val dialogIntent = Intent(this, Clan::class.java)
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(dialogIntent)
            setState(false)
        }
    }

    private inner class LongOperation : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg params: String): String {
            val client = UsersApi()
            client.basePath = API_BASE_PATH
            return client.sayHello()
        }

        override fun onPostExecute(result: String) {
//            val textik : TextView = root.findViewById(textik2) as TextView
//            textik.text = result
//            txt.text = "Executed" // txt.setText(result);
            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
        }

        override fun onPreExecute() {}

        override fun onProgressUpdate(vararg values: Void) {}
    }

    companion object {
        const val API_BASE_PATH = "http://192.168.1.111:8080/Mobile_Royal_Stats_Server_war_exploded/rest"
    }


}

