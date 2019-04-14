package com.ezrs.feature

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.media.ImageReader
import android.media.projection.MediaProjectionManager
import android.os.AsyncTask
import android.support.v4.app.ActivityCompat.startActivity
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.getSystemService
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.bsk.floatingbubblelib.FloatingBubbleTouchListener
import com.ezrs.feature.R.id.image
import io.swagger.client.api.PlayersApi
import io.swagger.client.model.Tag
import io.swagger.client.model.UserStat


class BubbleUtils(var context: Context,var root: View): FloatingBubbleTouchListener  {

    lateinit private var playerStats:UserStat

    override fun onUp(x: Float, y: Float) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRemove() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMove(x: Float, y: Float) {
        TODO(reason = "not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDown(x: Float, y: Float) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onTap(expanded: Boolean) {
//        drawScrShoot(makeScreenShot())
    }

    fun updatePlayerStatsLayout(root: View,playerStats: UserStat){
        var nameID = root.findViewById(R.id.nameIDPlayerStats) as TextView
        nameID.text="${playerStats.name} / ${playerStats.tag}"

        var clan = root.findViewById(R.id.clanPlayerStats) as TextView
        clan.text=playerStats.clan.name
    }


}
