package com.example.roadishome

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler;
import androidx.fragment.app.FragmentActivity

class loadPageActivity : Activity() {

    val handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_page)
        handler.postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
        }, 3500)
    }
}