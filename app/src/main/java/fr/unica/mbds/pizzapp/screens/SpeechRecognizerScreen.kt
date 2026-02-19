package fr.unica.mbds.pizzapp.screens

import android.Manifest.permission.RECORD_AUDIO
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import fr.unica.mbds.pizzapp.BuildConfig
import fr.unica.mbds.pizzapp.model.ChatMessage
import fr.unica.mbds.pizzapp.model.Pizza
import fr.unica.mbds.pizzapp.viewModel.ChatViewModel
import fr.unica.mbds.pizzapp.viewModel.PizzaViewModel
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


@Composable
fun MessagesList(messages: List<ChatMessage>) {
    Column(
        modifier = Modifier

            .fillMaxWidth()
            .background(Color(0xFFF0F0F0))
            .padding(8.dp)

    ) {
        messages.forEach { message ->
            ChatBubble(message = message)
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}
@Composable
fun ChatBubble(message: ChatMessage) {
    val bubbleColor = if (message.isUser) Color(0xFFD1E7DD) else Color(0xFFFFFFFF)
    val alignment = if (message.isUser) Alignment.CenterEnd else Alignment.CenterStart

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = alignment
    ) {
        Text(
            text = message.text,
            color = Color.Black,
            modifier = Modifier
                .background(bubbleColor, shape = MaterialTheme.shapes.medium)
                .padding(12.dp)
                .widthIn(max = 250.dp)
        )
    }
}


@Composable
fun SpeechRecognizerScreen(pizzaViewModel: PizzaViewModel,chatViewModel: ChatViewModel) {
    var textResult by remember { mutableStateOf("Appuie sur le bouton et parle...") }
    var isRecording by remember { mutableStateOf(false) }  // State for recording
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                textResult = "Permission accordée. Appuie sur le bouton."
            } else {
                textResult = "Permission refusée. Veuillez activer la permission dans les paramètres."
            }
        }
    )

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MessagesList(messages = chatViewModel.messages)
        Text(
            text = textResult,
            fontSize = 18.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .animateContentSize()  // Animates text size change
        )

        if (isRecording) {
            // Animation or feedback while recording
            CircularProgressIndicator(modifier = Modifier.padding(bottom = 16.dp))
            Text(text = "Enregistrement en cours...", fontSize = 16.sp)
        }

        Button(
            onClick = {
                // Vérifier la permission
                if (ContextCompat.checkSelfPermission(
                        context,
                        RECORD_AUDIO
                    ) == PermissionChecker.PERMISSION_GRANTED
                ) {
                    // Prevent multiple clicks during recording
                    isRecording = true
                    startSpeechRecognition(context,chatViewModel,pizzaViewModel) { result ->
                        textResult = result
                        isRecording = false
                    }
                } else {
                    launcher.launch(RECORD_AUDIO)
                }
            },
            enabled = !isRecording  // Disable button during recording
        ) {
            Text(text = if (isRecording) "Enregistrement..." else "Commencer à parler")
        }
    }
}


fun startSpeechRecognition(context: android.content.Context, chatViewModel: ChatViewModel,viewModel: PizzaViewModel, onResult: (String) -> Unit) {
    val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
    val speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fr-FR")
        putExtra(RecognizerIntent.EXTRA_PROMPT, "Parle maintenant...")
    }

    speechRecognizer.setRecognitionListener(object : android.speech.RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {}
        override fun onBeginningOfSpeech() {}
        override fun onRmsChanged(rmsdB: Float) {}
        override fun onBufferReceived(buffer: ByteArray?) {}
        override fun onEndOfSpeech() {}
        override fun onError(error: Int) {
            val errorMessage = when (error) {
                SpeechRecognizer.ERROR_AUDIO -> "Problème audio détecté. Veuillez réessayer."
                SpeechRecognizer.ERROR_CLIENT -> "Erreur client. Vérifiez votre microphone."
                SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Permission de microphone refusée."
                SpeechRecognizer.ERROR_NETWORK -> "Erreur réseau. Vérifiez votre connexion."
                SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "La connexion réseau a expiré."
                SpeechRecognizer.ERROR_NO_MATCH -> "Aucune commande reconnue. Parlez distinctement."
                SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Reconnaissance en cours... Attendez un instant."
                SpeechRecognizer.ERROR_SERVER -> "Erreur serveur. Réessayez plus tard."
                SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "Temps d'écoute expiré. Parlez plus rapidement."
                else -> "Erreur inconnue. Veuillez réessayer."
            }

            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            onResult("")
        }

        override fun onResults(results: Bundle?) {
            val text = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.get(0)
            onResult("")
            chatViewModel.addUserMessage(text ?: "Aucun résultat")
            appelerGemini(text?:"Bonjour Gemini, peux-tu m’aider ?",viewModel) { resume ->
                onResult("")  // Affiche le résumé dans ton chat
                chatViewModel.addBotMessage(resume)
            }


        }

        override fun onPartialResults(partialResults: Bundle?) {}
        override fun onEvent(eventType: Int, params: Bundle?) {}
    })

    speechRecognizer.startListening(speechIntent)


}
fun appelerGemini(s: String,viewModel: PizzaViewModel,onResult: (String) -> Unit) {
    val userInput = "Je veux une pizza Diavola avec du fromage supplémentaire et des olives."
    val prompt = """
                Mode : Commande de Pizza. -Tu es un assistant de commande de pizzas chaleureux. Nom "Pizzeria Da Leo"
                Voici les pizzas disponibles avec leurs prix :
Pizza ("Margherita", 8.0, R.drawable.pizza1),
Pizza ("Capricciosa", 10.0, R.drawable.pizza2),
Pizza ("Diavola", 9.0, R.drawable.pizza3),
Pizza ("Quattro Stagioni", 11.0, R.drawable.pizza4),
Pizza ("Quattro Formaggi", 12.0, R.drawable.pizza5),
Pizza ("Marinara", 7.0, R.drawable.pizza6),
Pizza ("Pepperoni", 9.0, R.drawable.pizza7),
Pizza ("Prosciutto", 10.0, R.drawable.pizza8),
Pizza("Frutti di Mare", 13.0, R.drawable.pizza9),

Tâche : Analyse la commande du client et retourne la liste de pizzas commandées avec les quantités et quantite cheese

Règles:
Ne prend pas en compte si  Extra_cheese est supérieur à 100 grammes
Ne dis pas objet JSON 
Sépare le résumé de la commande et le JSON
Quand l'utilisateur dit une commande (par exemple "Je veux une Margherita avec olives"), comprends son intention 

Commande actuelle : ${viewModel.cart.joinToString { "${it.first.name} : ${it.second} (Cheese: ${it.third})" }}

Réponse attendue :
renvoie un résumé clair de la commande  et split avec le mot json puis le json :
[
    {"name": "Margherita", "quantity": 2, "cheeseQuantity": 3},
    {"name": "Pepperoni", "quantity": 1, "cheeseQuantity": 0},
    {"name": "Hawaïenne", "quantity": 4, "cheeseQuantity": 0}
]

Commande utilisateur : $s
""".trimIndent()
    val jsonPart = JSONObject()
    jsonPart.put("text", prompt)

    val partsArray = JSONArray()
    partsArray.put(jsonPart)

    val contentObject = JSONObject()
    contentObject.put("parts", partsArray)

    val contentsArray = JSONArray()
    contentsArray.put(contentObject)

    val finalBody = JSONObject()
    finalBody.put("contents", contentsArray)
    val client = OkHttpClient()

    val apiKey = BuildConfig.GEMINI_API_KEY  // ← utilise BuildConfig ici
    Log.e("GeminiAPI", "Prompt : $prompt")
    val jsonBody = """
        {
          "contents": [{
            "parts": [{
              "text": "$prompt"
            }]
          }]
        }
        """

    val requestBody = finalBody.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
    val request = Request.Builder()
        .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=$apiKey")
        .post(requestBody)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.e("GeminiAPI", "Erreur réseau : ${e.message}")
        }

        override fun onResponse(call: Call, response: Response) {
            val responseBody = response.body?.string()
            if (response.isSuccessful && responseBody != null) {
                try {
                    val json = JSONObject(responseBody)
                    val text = json.getJSONArray("candidates")
                        .getJSONObject(0)
                        .getJSONObject("content")
                        .getJSONArray("parts")
                        .getJSONObject(0)
                        .getString("text")

                    Log.d("GeminiAPI", "Texte complet reçu : $text")



                    responseBody?.let {
                        parseGeminiResponse(it, viewModel,onResult)
                    }

                    // Affiche uniquement le résumé dans ton chat

                } catch (e: Exception) {
                    Log.e("GeminiAPI", "Erreur parsing : ${e.message}")
                }
            } else {
                Log.e("GeminiAPI", "Erreur HTTP : ${response.message}")
            }

        }
    })
}
fun parseGeminiResponse(response: String, viewModel: PizzaViewModel,onResult: (String) -> Unit) {
    try {
        // Parser la réponse JSON complète
        val jsonResponse = JSONObject(response)
        val candidatesArray = jsonResponse.getJSONArray("candidates")
        val contentText = candidatesArray
            .getJSONObject(0)
            .getJSONObject("content")
            .getJSONArray("parts")
            .getJSONObject(0)
            .getString("text")

        Log.d("GeminiResponse", "Texte reçu : $contentText")

        // Séparer la partie résumé et la partie JSON
        val summaryText = contentText.substringBefore("json").trim()
        val jsonPart = contentText.substringAfter("json").substringAfter("```json").substringBefore("```").trim()
        Log.d("GeminiResponse", "Résumé : $summaryText")
        Log.d("GeminiResponse", "JSON extrait : $jsonPart")
        onResult(summaryText)
        // Convertir cette partie JSON en tableau

        val ordersArray = JSONArray(jsonPart)
        val updatedCart = mutableListOf<Triple<Pizza, Int, Int>>()

        for (i in 0 until ordersArray.length()) {
            val orderObject = ordersArray.getJSONObject(i)
            val name = orderObject.getString("name")
            val quantity = orderObject.getInt("quantity")
            val cheeseQuantity = orderObject.getInt("cheeseQuantity")

            // Récupérer la pizza correspondante dans le menu
            val pizza = viewModel.pizzas.find { it.name.equals(name, ignoreCase = true) }
            if (pizza != null) {
                updatedCart.add(Triple(pizza, quantity, cheeseQuantity))
            } else {
                Log.e("GeminiResponse", "Pizza inconnue : $name")
            }
        }

        // Mettre à jour le panier dans le ViewModel
        viewModel.updateCartFromResponse(updatedCart, summaryText)
    } catch (e: Exception) {
        e.printStackTrace()
        Log.e("GeminiResponse", "Erreur lors de l'analyse de la réponse : ${e.message}")
    }
}
