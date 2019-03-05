package com.ezrs.feature


import android.app.Service
import android.content.Context;

import android.graphics.Color;

import android.support.v4.content.ContextCompat;

import android.view.Gravity;

import android.view.WindowManager;


import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.bsk.floatingbubblelib.FloatingBubbleService
import com.bsk.floatingbubblelib.FloatingBubbleConfig






class MyService : FloatingBubbleService() {
       // Service() {


    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.d("aa","tu som")

        return super.onStartCommand(intent, flags, startId)
    }


//    override fun getConfig(): FloatingBubbleConfig {
//        return FloatingBubbleConfig.Builder()
//                .bubbleIcon(ContextCompat.getDrawable(context, R.drawable.web_icon))
//
//                .removeBubbleIcon(ContextCompat.getDrawable(context, com.bsk.floatingbubblelib.R.drawable.close_default_icon))
//
//                .bubbleIconDp(54)
//
//                  .expandableView(null)
//
//                .removeBubbleIconDp(54)
//
//                .paddingDp(4)
//
//                .borderRadiusDp(0)
//
//                .physicsEnabled(true)
//
//                .expandableColor(Color.WHITE)
//
//                .triangleColor(10)
//
//                .gravity(Gravity.LEFT)
//
//                .build()
//    }


}

