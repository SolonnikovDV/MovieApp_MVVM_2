package com.example.movieapp_mvvm.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.movieapp_mvvm.R
import com.example.movieapp_mvvm.db.MovieDataBase
import com.example.movieapp_mvvm.repository.MovieRepository
import com.example.movieapp_mvvm.ui.fragments.MoviesFragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_movie.*

class MovieActivity : AppCompatActivity() {

    val TAG = "new_token"

    lateinit var viewModel: MovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_movie)

//        getToken()

        val movieRepository = MovieRepository(MovieDataBase(this))
        val viewModelProviderFactory = MovieViewModelProviderFactory(application, movieRepository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(MovieViewModel::class.java)
        bottomNavigationView.setupWithNavController(moviesNavHostFragment.findNavController())
    }

    fun getToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            Log.w(TAG, token.toString())
        })
        FirebaseMessaging.getInstance().subscribeToTopic("news")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.getSerializableExtra("seeLaterItem")
        val seeLaterList = MoviesFragment.Companion.seeLaterList

        if (seeLaterList.size > 0) {
            val position = seeLaterList.size - 1
            val bundle = Bundle().apply {
                putSerializable("details", seeLaterList[position])
            }
            findNavController(MoviesFragment()).navigate(
                R.id.action_moviesFragment_to_detailsFragment,
                bundle
            )

        } else {
            Toast.makeText(this, "Reminder list is empty", Toast.LENGTH_LONG).show()
        }
    }
}