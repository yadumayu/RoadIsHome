package com.example.roadishome

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.gson.Gson

object SharedPreferencesHepler {
    private const val SHARED_PREFS_NAME = "my_shared_prefs"
    private const val KEY_GOOGLE_ACCOUNT = "google_account"

    fun saveGoogleAccount(context: Context, account: GoogleSignInAccount) {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(account)
        editor.putString(KEY_GOOGLE_ACCOUNT, json)
        editor.apply()
    }

    fun getGoogleAccount(context: Context): GoogleSignInAccount? {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString(KEY_GOOGLE_ACCOUNT, null)
        return gson.fromJson(json, GoogleSignInAccount::class.java)
    }

    fun clearGoogleAccount(context: Context) {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(KEY_GOOGLE_ACCOUNT)
        editor.apply()
    }
}