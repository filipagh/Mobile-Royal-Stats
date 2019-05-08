package com.ezrs.feature



import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.swagger.client.ApiException
import io.swagger.client.api.ConditionsApi
import io.swagger.client.api.PlayersApi
import io.swagger.client.model.ConditionView
import io.swagger.client.model.Tag
import io.swagger.client.model.UserStat
import java.util.*
import java.util.concurrent.ExecutionException
import io.swagger.client.ApiInvoker
import okhttp3.WebSocket

/**
 * service na obsluhu bublinky
 */
class MyService : FloatingBubbleService(), Tab1.OnFragmentInteractionListener {
    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    // Service() {

    lateinit var root: View
    lateinit var rootPlayerStats: View
    lateinit var playerStatsContent: UserStat
    lateinit var bubbleUtils: BubbleUtils
    lateinit var ws : WebSocket

    override fun onBind(intent: Intent): IBinder? {
        return null
    }





    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        //odchytenie player tag z nacitania scrshotu
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                IntentFilter("PlayerStatsTag"))
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                IntentFilter("PlayerStats"))

        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkStateReceiver, filter)

        return super.onStartCommand(intent, flags, startId)
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
        var value = getSharedPreferences(MainActivity.TASK_PREFERENCE, Activity.MODE_PRIVATE).getString(ConditionsAdapter.DeleteConditionTask.PREFERENCE_KEY, "")
        getSharedPreferences(MainActivity.TASK_PREFERENCE, Activity.MODE_PRIVATE).edit().remove(ConditionsAdapter.DeleteConditionTask.PREFERENCE_KEY).apply()
        var toDelete = ArrayList<Int>()
        val gson = Gson()
        if (value != "") {
            val turnsType = object : TypeToken<ArrayList<Int>>() {}.type
            toDelete = gson.fromJson<ArrayList<Int>>(value, turnsType)
            toDelete.forEach { v -> ConditionsAdapter.DeleteConditionTask(v, applicationContext).execute() }
        }

        value = getSharedPreferences(MainActivity.TASK_PREFERENCE, Activity.MODE_PRIVATE).getString(CreateCondition.PREFERENCE_KEY, "")
        getSharedPreferences(MainActivity.TASK_PREFERENCE, Activity.MODE_PRIVATE).edit().remove(CreateCondition.PREFERENCE_KEY).apply()
        var toCreate = ArrayList<ConditionView>()
        if (value != "") {
            val turnsType = object : TypeToken<ArrayList<ConditionView>>() {}.type
            toCreate = gson.fromJson<ArrayList<ConditionView>>(value, turnsType)
            toCreate.forEach { v -> CreateConditionTask(v).execute() }
        }
//        tasks.forEach { v -> v.execute() }
    }

    inner class CreateConditionTask internal constructor(private val v: ConditionView) : AsyncTask<Void, Void, Result<ConditionView>>() {

        override fun doInBackground(vararg params: Void): Result<ConditionView> {
            val api = ConditionsApi()
            api.basePath = MyService.API_BASE_PATH
            return try {
                if (v.id == null) {
                    Result.success(api.create(v, getSharedPreferences(LoginActivity.PREFERENCE, Activity.MODE_PRIVATE).getString(LoginActivity.APIKEY, "")))
                } else {
                    Result.success(api.update(v, getSharedPreferences(LoginActivity.PREFERENCE, Activity.MODE_PRIVATE).getString(LoginActivity.APIKEY, "")))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

        override fun onPostExecute(r: Result<ConditionView>) {
            try {
                val success = r.getOrThrow()
            } catch (e: ApiException) {
            } catch (e: ExecutionException) {
                val value = getSharedPreferences(MainActivity.TASK_PREFERENCE, Activity.MODE_PRIVATE).getString(CreateCondition.PREFERENCE_KEY, "")
                var list = ArrayList<ConditionView>()
                val gson = Gson()
                if (value != "") {
                    val turnsType = object : TypeToken<ArrayList<ConditionView>>() {}.type
                    list = gson.fromJson<ArrayList<ConditionView>>(value, turnsType)
                }
                list.add(v)
                getSharedPreferences(MainActivity.TASK_PREFERENCE, Activity.MODE_PRIVATE).edit().putString(CreateCondition.PREFERENCE_KEY, gson.toJson(list)).apply()
            }
        }
    }

    // handlovanie udalosti
    private val mMessageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Get extra data included in the Intent
            val json = intent.getStringExtra("json")
            val tag = intent.getStringExtra("Tag")
            Log.d("receiver", "Got message: $tag")
            if (!tag.isNullOrEmpty()) {
                val t = Tag()
                t.tag = tag
                ws.send(Gson().toJson(t))
//                val task = LoadPlayerStatsTask(tag)
//                task.execute()
            } else if (!json.isNullOrEmpty()) {
                bubbleUtils.updatePlayerStatsLayout(rootPlayerStats, ApiInvoker.deserialize(json, "", UserStat::class.java)as UserStat )
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
        ws.close(1000,"bye")
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver)
        super.onDestroy()
    }

    // konfiguracia bublinky
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
        val webSocketCli = WebSocketCli()
        ws = webSocketCli.run(bubbleUtils, rootPlayerStats,context)

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

    /**
     * async task for our api (playerStats)
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

        const val API_BASE_PATH = "http://192.168.0.213:8080/Mobile_Royal_Stats_Server_war_exploded/rest"

    }


}

