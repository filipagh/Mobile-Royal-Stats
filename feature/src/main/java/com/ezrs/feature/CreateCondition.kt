package com.ezrs.feature

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.app.Activity
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import io.swagger.client.ApiException
import io.swagger.client.api.ConditionsApi
import io.swagger.client.model.ConditionView
import kotlinx.android.synthetic.main.activity_create_condition.*

class CreateCondition : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_condition)
        val fields: Spinner = findViewById(R.id.field) as Spinner
        ArrayAdapter.createFromResource(this, R.array.fields_array,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            fields.adapter = adapter
        }
        val operator: Spinner = findViewById(R.id.operator) as Spinner
        ArrayAdapter.createFromResource(this, R.array.operators_array,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            operator.adapter = adapter
        }

    }

    fun createCondition(v: View) {
        val c = ConditionView()
        c.name = name.text.toString()
        c.field = field.selectedItem.toString()
        c.operator = operator.selectedItem.toString()
        c.value = value.text.toString()
        val task = CreateConditionTask(c)
        task.execute()
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

            create_condition_progress.visibility = if (show) View.VISIBLE else View.GONE
            create_condition_progress.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 1 else 0).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            create_condition_progress.visibility = if (show) View.VISIBLE else View.GONE
                        }
                    })
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            create_condition_progress.visibility = if (show) View.VISIBLE else View.GONE
//            clan_form.visibility = if (show) View.GONE else View.VISIBLE
        }
    }

    inner class CreateConditionTask internal constructor(private val v: ConditionView) : AsyncTask<Void, Void, Result<ConditionView>>() {

        override fun doInBackground(vararg params: Void): Result<ConditionView> {
            // TODO: attempt authentication against a network service.
            val api = ConditionsApi()
            api.basePath = MyService.API_BASE_PATH
            return try {
                Result.success(api.create(v, getSharedPreferences(LoginActivity.PREFERENCE, Activity.MODE_PRIVATE).getString(LoginActivity.APIKEY, "")))
            } catch (e: ApiException) {
                Result.failure(e)
            }

        }

        override fun onPostExecute(r: Result<ConditionView>) {
            try {
                val success = r.getOrThrow()
                finish()
            } catch (e: ApiException) {
                name.error = "Something went wrong with code: " + e.code
            } finally {
                showProgress(false)
            }
        }

        override fun onCancelled() {
            showProgress(false)
        }
    }
}
