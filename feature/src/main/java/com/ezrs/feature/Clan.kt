package com.ezrs.feature

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import io.swagger.client.ApiException
import io.swagger.client.api.ClansApi
import io.swagger.client.api.ConditionsApi
import io.swagger.client.model.ClanStats
import io.swagger.client.model.ClanView
import io.swagger.client.model.ConditionView
import io.swagger.client.model.Tag
import kotlinx.android.synthetic.main.activity_clan.*
import kotlinx.android.synthetic.main.clan_conditions.*
import kotlinx.android.synthetic.main.clan_new.*
import kotlinx.android.synthetic.main.clan_players.*
import java.util.concurrent.ExecutionException


class Clan : Activity() {

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var vm: RecyclerView.LayoutManager
    private lateinit var clanPlayerLayoutManager: RecyclerView.LayoutManager
    private lateinit var userClanStats : ClanView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clan)
        pager.offscreenPageLimit = 5
    }


    private fun refreshData() {
        vm = LinearLayoutManager(this)
        GetClanConditions().execute()
    }

    private fun refreshClanPlayersData() {
        clanPlayerLayoutManager = LinearLayoutManager(this)
        GetClanPlayersTask().execute()
    }

    override fun onResume() {
        super.onResume()
        var viewp: ViewPager = findViewById(R.id.pager) as ViewPager
        viewp.adapter = CustomPagerAdapter(this)
        val clanTask = GetClanTask()
        clanTask.execute()



        val listener = object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                if(position == 1) {
                    val pullToRefresh = findViewById(R.id.clanPlayerSwipeLayout) as SwipeRefreshLayout
                    pullToRefresh.setOnRefreshListener {
                        refreshClanPlayersData()
                        pullToRefresh.isRefreshing = false
                    }
                }
                if(position == 2) {
                    val pullToRefresh = findViewById(R.id.swipeToRefresh) as SwipeRefreshLayout
                    pullToRefresh.setOnRefreshListener {
                        refreshData() // your code
                        pullToRefresh.isRefreshing = false
                    }
                }
            }
        }

        viewp.addOnPageChangeListener(listener)


//        button5.setOnClickListener { createClan()}
    }

    fun createClan(v: View) {
        val clan = ClanView()
        clan.name = editText2.text.toString()
        clan.gameId = game_id.text.toString()
        clan.token = token.text.toString()
        showProgress(true)
        val clanTask = CreateClanTask(clan)
        clanTask.execute()
    }

    fun createCondition(v: View) {
        val intent = Intent(this, CreateCondition::class.java)
        startActivity(intent)
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress(show: Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

//            clan_form.visibility = if (show) View.GONE else View.VISIBLE
//            clan_form.animate()
//                    .setDuration(shortAnimTime)
//                    .alpha((if (show) 0 else 1).toFloat())
//                    .setListener(object : AnimatorListenerAdapter() {
//                        override fun onAnimationEnd(animation: Animator) {
//                            clan_form.visibility = if (show) View.GONE else View.VISIBLE
//                        }
//                    })

            clan_progress.visibility = if (show) View.VISIBLE else View.GONE
            clan_progress.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 1 else 0).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            clan_progress.visibility = if (show) View.VISIBLE else View.GONE
                        }
                    })
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            clan_progress.visibility = if (show) View.VISIBLE else View.GONE
//            clan_form.visibility = if (show) View.GONE else View.VISIBLE
        }
    }


    inner class CreateClanTask internal constructor(private val clan: ClanView) : AsyncTask<Void, Void, Result<ClanView>>() {
        override fun doInBackground(vararg params: Void): Result<ClanView> {
            // TODO: attempt authentication against a network service.
            val api = ClansApi()
            api.basePath = MyService.API_BASE_PATH

            return try {
                Result.success(api.create(clan, getSharedPreferences(LoginActivity.PREFERENCE, Activity.MODE_PRIVATE).getString(LoginActivity.APIKEY, "")))
            } catch (e: ApiException) {
                Result.failure(e)
            }
        }

        override fun onPostExecute(r: Result<ClanView>) {
            try {
                val success = r.getOrThrow()
                textView4.text = success.name
            } catch (e: ApiException) {
                when (e.code) {
                    400 -> textView4.text = "Something is missing"
                    404 -> textView4.text = "No clan"
                    401 -> textView4.text = "Unauthorized"
                    500 -> textView4.text = "Something is fucked up"
                }
            } finally {
                showProgress(false)
            }
        }

        override fun onCancelled() {
//            mAuthTask = null
            showProgress(false)
        }
    }

    inner class GetClanTask : AsyncTask<Void, Void, Result<ClanView>>() {

        override fun doInBackground(vararg params: Void): Result<ClanView> {
            // TODO: attempt authentication against a network service.
            val api = ClansApi()
            api.basePath = MyService.API_BASE_PATH

            return try {
                Result.success(api.myClan(getSharedPreferences(LoginActivity.PREFERENCE, Activity.MODE_PRIVATE).getString(LoginActivity.APIKEY, "")))
            } catch (e: Exception) {
                Result.failure(e)
            }

        }

        override fun onPostExecute(r: Result<ClanView>) {
            try {
                val success = r.getOrThrow()
                userClanStats = success
                refreshData()
                refreshClanPlayersData()
//                textView4.text = success.name
            } catch (e: Exception) {
//                }
            } finally {
//                showProgress(false)
            }
        }

        override fun onCancelled() {
//            mAuthTask = null
            showProgress(false)
        }
    }

    inner class GetClanConditions : AsyncTask<Void, Void, Result<List<ConditionView>>>() {

        override fun doInBackground(vararg params: Void): Result<List<ConditionView>> {
            // TODO: attempt authentication against a network service.
            val api = ConditionsApi()
            api.basePath = MyService.API_BASE_PATH

            return try {
                Result.success(api.list(getSharedPreferences(LoginActivity.PREFERENCE, Activity.MODE_PRIVATE).getString(LoginActivity.APIKEY, "")))
            } catch (e: Exception) {
                Result.failure(e)
            }

        }

        override fun onPostExecute(r: Result<List<ConditionView>>) {
            try {
                val success = r.getOrThrow()

                viewAdapter = ConditionsAdapter(success)

                listView.apply {
                    // use this setting to improve performance if you know that changes
                    // in content do not change the layout size of the RecyclerView
                    setHasFixedSize(true)

                    // use a linear layout manager
                    layoutManager = vm

                    // specify an viewAdapter (see also next example)
                    adapter = viewAdapter

                }


//                textView4.text = success.name
            } catch (e: ApiException) {
            } catch (e: ExecutionException) {
            } finally {
//                showProgress(false)
            }
        }

        override fun onCancelled() {
//            mAuthTask = null
            showProgress(false)
        }
    }

    inner class GetClanPlayersTask : AsyncTask<Void, Void, Result<ClanStats>>() {

        override fun doInBackground(vararg params: Void): Result<ClanStats> {
            // TODO: attempt authentication against a network service.
            val api = ClansApi()
            api.basePath = MyService.API_BASE_PATH

            return try {
                val tag = Tag()
                tag.tag=userClanStats.gameId
                Result.success(api.info(tag))
            } catch (e: ApiException) {
                Result.failure(e)
            }

        }

        override fun onPostExecute(r: Result<ClanStats>) {
            try {
                val success = r.getOrThrow()

                viewAdapter = ClanPlayersAdapter(success)

                clanPlayerRecyclerview.apply {
                    // use this setting to improve performance if you know that changes
                    // in content do not change the layout size of the RecyclerView
                    setHasFixedSize(true)

                    // use a linear layout manager
                    layoutManager = clanPlayerLayoutManager

                    // specify an viewAdapter (see also next example)
                    adapter = viewAdapter

                }
//
            } catch (e: ApiException) {
                when (e.code) {
//                    400 -> textView4.text = "Something is missing"
//                    404 -> textView4.text = "No clan"
//                    401 -> textView4.text = "Unauthorized"
//                    500 -> textView4.text = "Something is fucked up"
                }
            } finally {
//                showProgress(false)
            }
        }

        override fun onCancelled() {
//            mAuthTask = null
            showProgress(false)
        }
    }

}
