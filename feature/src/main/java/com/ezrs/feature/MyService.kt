package com.ezrs.feature



import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.AsyncTask
import android.os.IBinder
import android.support.v4.content.ContextCompat
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.widget.NestedScrollView
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import com.bsk.floatingbubblelib.FloatingBubbleConfig
import com.bsk.floatingbubblelib.FloatingBubbleService
import io.swagger.client.api.PlayersApi
import io.swagger.client.api.UsersApi
import io.swagger.client.model.Tag
import io.swagger.client.model.UserStat


class MyService : FloatingBubbleService(), Tab1.OnFragmentInteractionListener {
    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    // Service() {

    lateinit var root: View
    lateinit var rootPlayerStats: View
    lateinit var playerStatsContent: UserStat
    lateinit var bubbleUtils: BubbleUtils

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("aa", "tu som")

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                IntentFilter("PlayerStatsTag"))

        return super.onStartCommand(intent, flags, startId)


    }

    private val mMessageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Get extra data included in the Intent
            val tag = intent.getStringExtra("Tag")
            Log.d("receiver", "Got message: $tag")
            if (!tag.isNullOrEmpty()) {
                val task = LoadPlayerStatsTask(tag)
                task.execute()
                rootPlayerStats.visibility = View.VISIBLE
                setState(true)
            } else {
                rootPlayerStats.visibility = View.GONE
                setState(true)
            }
        }
    }


    override fun onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver)
        super.onDestroy()
    }


    override fun getConfig(): FloatingBubbleConfig {
        root = getInflater().inflate(R.layout.test1, null)
        val gombik = Button(context)

        var button: Button? = null
        button = root.findViewById(R.id.button) as Button

        button.setOnClickListener {
            klik(root)
        }

        val playerStatsSlot = root.findViewById(R.id.playerStatsSlot) as NestedScrollView
        rootPlayerStats = getInflater().inflate(R.layout.player_stats, playerStatsSlot)
        rootPlayerStats.visibility = View.GONE

        bubbleUtils = BubbleUtils(context, root)

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

    fun scrShot(vstup: View) {
        val dialogIntent = Intent(this, PlayerStatsActivity::class.java)
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(dialogIntent)
        setState(false)
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

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    inner class LoadPlayerStatsTask internal constructor(val tag: String) : AsyncTask<Void, Void, UserStat>() {

        override fun doInBackground(vararg params: Void): UserStat? {
            // TODO: attempt authentication against a network service.
            val api = PlayersApi()
            api.basePath = MyService.API_BASE_PATH
            val t = Tag()
            t.tag = tag
            try {
                return api.info(t)
            } catch (e:Exception)
            {
                Log.d("EX","async task ${e.toString()}")
                cancel(true)
                return null
            }
        }

        override fun onPostExecute(success: UserStat) {
            bubbleUtils.updatePlayerStatsLayout(rootPlayerStats, success)
        }

        override fun onCancelled() {
         val w =5
        cancel(true)
//            mAuthTask = null
//            showProgress( false)
        }
    }

    companion object {
        const val API_BASE_PATH = "http://192.168.1.111:8080/Mobile_Royal_Stats_Server_war_exploded/rest"
    }


}

