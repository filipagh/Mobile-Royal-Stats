package com.ezrs.feature

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.app.Activity
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.View
import io.swagger.client.ApiException
import io.swagger.client.api.ClansApi
import io.swagger.client.model.ClanView
import kotlinx.android.synthetic.main.clan_new.*

class Clan : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clan)
    }

    override fun onResume() {
        super.onResume()
        var viewp: ViewPager = findViewById(R.id.pager) as ViewPager
        viewp.adapter = CustomPagerAdapter(this)
        val clanTask = GetClanTask()
        clanTask.execute()
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
//                showProgress(false)
            }
        }

        override fun onCancelled() {
//            mAuthTask = null
            showProgress(false)
        }
    }

}
