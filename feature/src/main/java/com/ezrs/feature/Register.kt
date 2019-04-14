package com.ezrs.feature

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.app.Activity
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import io.swagger.client.api.UsersApi
import io.swagger.client.model.User
import io.swagger.client.model.UserView
import kotlinx.android.synthetic.main.activity_register.*

class Register : Activity() {

    private var mAuthTask: UserRegisterTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    fun register(view: View) {
        if (mAuthTask != null) {
            return
        }

        // Reset errors.
        register_email.error = null
        register_password.error = null
        register_password2.error = null

        // Store values at the time of the login attempt.
        val emailStr = register_email.text.toString()
        val passwordStr = register_password.text.toString()
        val password2Str = register_password2.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(passwordStr) && !isPasswordValid(passwordStr)) {
            register_password.error = getString(R.string.error_invalid_password)
            focusView = register_password
            cancel = true
        }

        if (password2Str != passwordStr) {
            register_password2.error = getString(R.string.error_field_required)
            focusView = register_password2
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(emailStr)) {
            register_email.error = getString(R.string.error_field_required)
            focusView = register_email
            cancel = true
        } else if (!isEmailValid(emailStr)) {
            register_email.error = getString(R.string.error_invalid_email)
            focusView = register_email
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true)
            mAuthTask = UserRegisterTask(emailStr, passwordStr, register_name.toString())
            mAuthTask!!.execute(null as Void?)
        }
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

            register_form.visibility = if (show) View.GONE else View.VISIBLE
            register_form.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 0 else 1).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            register_form.visibility = if (show) View.GONE else View.VISIBLE
                        }
                    })

            register_progress.visibility = if (show) View.VISIBLE else View.GONE
            register_progress.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 1 else 0).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            register_progress.visibility = if (show) View.VISIBLE else View.GONE
                        }
                    })
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            register_progress.visibility = if (show) View.VISIBLE else View.GONE
            register_form.visibility = if (show) View.GONE else View.VISIBLE
        }
    }

    private fun isEmailValid(email: String): Boolean {
        //TODO: Replace this with your own logic
        return email.contains("@")
    }

    private fun isPasswordValid(password: String): Boolean {
        //TODO: Replace this with your own logic
        return password.length > 4
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    inner class UserRegisterTask internal constructor(private val mEmail: String, private val mPassword: String, private val mName: String) : AsyncTask<Void, Void, UserView>() {

        override fun doInBackground(vararg params: Void): UserView {
            // TODO: attempt authentication against a network service.
            val api = UsersApi()
            api.basePath = MyService.API_BASE_PATH
            val u = User()
            u.email = mEmail
            u.password = mPassword
            u.name = mName
            return api.create(u)
        }

        override fun onPostExecute(success: UserView) {
            (findViewById(R.id.email_login_form)as LinearLayout).visibility = View.GONE
            (findViewById(R.id.logged_in_view)as LinearLayout).visibility = View.VISIBLE
            (findViewById(R.id.logged_in_text)as TextView).text = success.apiKey
        }

        override fun onCancelled() {
            mAuthTask = null
            showProgress(false)
        }
    }

    companion object {

        /**
         * Id to identity READ_CONTACTS permission request.
         */
        private val REQUEST_READ_CONTACTS = 0

        /**
         * A dummy authentication store containing known user names and passwords.
         * TODO: remove after connecting to a real authentication system.
         */
        private val DUMMY_CREDENTIALS = arrayOf("foo@example.com:hello", "bar@example.com:world")
    }

}
