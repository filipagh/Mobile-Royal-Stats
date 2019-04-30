package com.ezrs.feature

import android.content.Context
import android.view.View
import android.widget.TextView
import com.bsk.floatingbubblelib.FloatingBubbleTouchListener
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


    // refresh data
    fun updatePlayerStatsLayout(root: View,playerStats: UserStat){
        var nameID = root.findViewById(R.id.nameIDPlayerStats) as TextView
        nameID.text="${playerStats.name} / ${playerStats.tag}"

        var clan = root.findViewById(R.id.clanPlayerStats) as TextView
        clan.text="Clan ${playerStats.clan.name}"

        var warDayWins = root.findViewById(R.id.warDayWinsPlayerStats) as TextView
        warDayWins.text="WDW ${playerStats.games.warDayWins}"

        var cwLast10 = root.findViewById(R.id.cwLast10PlayerStats) as TextView
        cwLast10.text="Last-10:  ${playerStats.games.winsPercentLast10}"

        var cwLast20 = root.findViewById(R.id.cwLast20PlayerStats) as TextView
        cwLast20.text="Last-20:  ${playerStats.games.winsPercentLast20}"

        var cwLifeTime = root.findViewById(R.id.cwLifeTimePlayerStats) as TextView
        cwLifeTime.text="LifeTime: ${playerStats.games.winsPercentLiveTime}"

        var tropy = root.findViewById(R.id.trophyPlayerStats) as TextView
        tropy.text="Trophy ${playerStats.trophies}"

        var leader = root.findViewById(R.id.leaderPlayerStats) as TextView
        leader.text="Level ${playerStats.stats.level}"

        var missFinalBattle = root.findViewById(R.id.missedFinalBattlePlayerStats) as TextView
        missFinalBattle.text="FinalBattleMiss${playerStats.games.warDayBattleMiss}"
    }


}
