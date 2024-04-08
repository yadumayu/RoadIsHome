package com.example.roadishome

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.roadishome.databinding.ActivityMainBinding
import com.google.android.gms.common.SignInButton
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private final val RC_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val headerView: View = navView.getHeaderView(0)
        val signInButton: SignInButton = headerView.findViewById(R.id.sign_in_button)
        signInButton.setSize(SignInButton.SIZE_STANDARD)

        signInButton.setOnClickListener{
            val gso : GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions
                .DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

            val googleSignInClient : GoogleSignInClient = GoogleSignIn.getClient(this,gso)
            val signInIntent: Intent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Проверяем, что результат авторизации приходит от Google Sign-In
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    // Обработка результата авторизации
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)

            // Авторизация успешна, выполняйте дополнительные действия здесь
            updateNavHeader(account)
        } catch (e: ApiException) {
            // Авторизация не удалась, обработайте ошибку здесь
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }
    fun saveUserId(userId: String) {
        val sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("userId", userId)
        editor.apply()
    }
    private fun updateNavHeader(account: GoogleSignInAccount?) {
        // Находим NavigationView
        val navigationView: NavigationView = findViewById(R.id.nav_view)

        // Получаем шапку NavigationView
        val headerView: View = navigationView.getHeaderView(0)

        // Находим View-элементы для фотографии и имени пользователя
        val photoImageView: ImageView = headerView.findViewById(R.id.profileImageView)
        val nameTextView: TextView = headerView.findViewById(R.id.nameUserGoogle)
        val buttonSignIn: SignInButton = headerView.findViewById(R.id.sign_in_button)

        // Обновляем фотографию и имя пользователя, если пользователь авторизован
        if (account != null) {
            val photoUrl = account.photoUrl
            if (photoUrl != null) {
                // Загружаем фотографию пользователя с помощью Glide или другой библиотеки
                Glide.with(this).load(photoUrl).into(photoImageView)
            }
            val name = account.displayName
            if (name != null) {
                // Устанавливаем имя пользователя
                nameTextView.text = name
                nameTextView.visibility = View.VISIBLE
                buttonSignIn.visibility= View.GONE

            }
        } else {
            // Если пользователь не авторизован, сбрасываем фотографию и имя пользователя
            photoImageView.setImageResource(R.drawable.side_nav_bar)
            nameTextView.text = getString(R.string.app_name)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}