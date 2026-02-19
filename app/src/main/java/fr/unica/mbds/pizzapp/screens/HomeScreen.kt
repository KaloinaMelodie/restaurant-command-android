package fr.unica.mbds.pizzapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import fr.unica.mbds.pizzapp.Panier
import fr.unica.mbds.pizzapp.PizzaList
import fr.unica.mbds.pizzapp.R
import fr.unica.mbds.pizzapp.model.Pizza
import fr.unica.mbds.pizzapp.viewModel.ChatViewModel
import fr.unica.mbds.pizzapp.viewModel.CommandViewModel
import fr.unica.mbds.pizzapp.viewModel.PizzaViewModel

@Composable
fun HomeScreen(navController: NavController,pizzaViewModel: PizzaViewModel,chatViewModel: ChatViewModel,modifier: Modifier = Modifier) {
    val showDialog = remember { mutableStateOf(false) }
    Scaffold (
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog.value = true }, containerColor =  MaterialTheme.colorScheme.primary,

                ) {
                Icon(
                    painter = painterResource(id = R.drawable.support),
                    contentDescription = "Chat",
                    modifier = Modifier.size(30.dp),
                    tint = LocalContentColor.current // pour garder les couleurs originales du drawable

                )
            }
        }
        ){
        innerPadding ->
        Column (
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription ="Da leo",
                modifier = Modifier
                    .size(150.dp)

                    .align(Alignment.CenterHorizontally)
            )
            Text(text = "Da Leo", style = MaterialTheme.typography.headlineMedium)
            if (showDialog.value) {
                ModalDialog(onDismiss = { showDialog.value = false },chatViewModel=chatViewModel,pizzaViewModel)
            }
            Column(modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally){
                Button (onClick = { navController.navigate(PizzaList) }, modifier = Modifier.padding(8.dp)
                    .widthIn(min = 250.dp),
                ) {
                    Text("Voir le menu des pizzas")
                }
                Spacer(modifier = Modifier.padding(4.dp))
                Button (onClick = { navController.navigate(Panier) }, modifier = Modifier.padding(8.dp)
                    .widthIn(min = 250.dp)
                ) {
                    Text("Voir mon panier")
                }
                if (pizzaViewModel.cart.isNotEmpty()){
                    Button (onClick = { pizzaViewModel.clearCart()
                    }, modifier = Modifier.padding(8.dp)
                        .widthIn(min = 250.dp)
                    ) {
                        Text("Payer la commande")
                    }
                }

            }

        }
    }

}


@Composable
fun ModalDialog(onDismiss: () -> Unit,chatViewModel: ChatViewModel,viewModel: PizzaViewModel) {
    Dialog (onDismissRequest = onDismiss) {
        Box(modifier = Modifier.fillMaxSize().background(Color.White, shape = MaterialTheme.shapes.medium).verticalScroll(rememberScrollState())) {
            Column {
                Button(onClick = onDismiss,shape = CircleShape,contentPadding = PaddingValues(12.dp), modifier = Modifier.padding(12.dp)) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Chat"
                    )
                }
                SpeechRecognizerScreen(pizzaViewModel = viewModel, chatViewModel = chatViewModel)
            }

        }
    }
}
