package com.ezrs.feature

import android.util.Log
import android.view.View
import io.swagger.client.ApiInvoker
import io.swagger.client.model.UserStat
import okhttp3.*
import okio.ByteString


class WebSocketCli: WebSocketListener() {

    lateinit var bubbleUtils : BubbleUtils
    lateinit var rootPlayerStats : View

    fun run(bubbleUtilsIn : BubbleUtils,rootPlayerStatsIn : View): WebSocket {
        bubbleUtils=bubbleUtilsIn
        rootPlayerStats=rootPlayerStatsIn
        val c = OkHttpClient()

        val r = Request.Builder()
                .url("ws://pumec.zapto.org:8080/Mobile-Royal-Stats-Server_war/example")
                .build()
        val ws = c.newWebSocket(r, this)
        // Trigger shutdown of the dispatcher's executor so this process can exit cleanly.


        return ws
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {

    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.d("WS",text)
        bubbleUtils.updatePlayerStatsLayout(rootPlayerStats, ApiInvoker.deserialize(text, "", UserStat::class.java)as UserStat )
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        Log.d("WS",bytes.hex())
        bubbleUtils.updatePlayerStatsLayout(rootPlayerStats, ApiInvoker.deserialize(bytes.hex(), "", UserStat::class.java)as UserStat )
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(1000, null)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        t.printStackTrace()
    }

}