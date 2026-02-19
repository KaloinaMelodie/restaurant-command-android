package fr.unica.mbds.pizzapp

import android.Manifest.permission.RECORD_AUDIO
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.unica.mbds.pizzapp.screens.SpeechRecognizerScreen


import fr.unica.mbds.pizzapp.ui.theme.PizzAppTheme
import fr.unica.mbds.pizzapp.viewModel.ChatViewModel
import fr.unica.mbds.pizzapp.viewModel.CommandViewModel
import fr.unica.mbds.pizzapp.viewModel.PizzaViewModel
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

class MainActivity : ComponentActivity() {
    private val RECORD_AUDIO_REQUEST_CODE = 1

    private fun checkAudioPermission() {
        if (ContextCompat.checkSelfPermission(this, RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(RECORD_AUDIO),
                RECORD_AUDIO_REQUEST_CODE
            )
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PizzAppTheme {
                val pizzaViewModel: PizzaViewModel = viewModel<PizzaViewModel>()
                val chatViewModel: ChatViewModel = viewModel<ChatViewModel>()

                checkAudioPermission()

                   MyApp(pizzaViewModel=pizzaViewModel, chatViewModel = chatViewModel)

            }
        }

    }

}



