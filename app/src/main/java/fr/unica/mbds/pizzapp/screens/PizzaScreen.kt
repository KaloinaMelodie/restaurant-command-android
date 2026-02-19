package fr.unica.mbds.pizzapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.unica.mbds.pizzapp.model.Pizza
import fr.unica.mbds.pizzapp.viewModel.CommandViewModel
import fr.unica.mbds.pizzapp.viewModel.PizzaViewModel
import kotlinx.coroutines.launch

@Composable
fun PizzaDetail(pizza: Pizza, modifier: Modifier = Modifier, pizzaViewModel: PizzaViewModel =viewModel(), snackbarHostState: SnackbarHostState) {
    var extraCheese = remember { mutableStateOf(50) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(onClick = {
                pizzaViewModel.addToCart(pizza=pizza,quantity=1, cheeseQuantity = extraCheese.value)
//                commandeViewModel.ajouterPizzaCommande(pizza, extraCheese.value)
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Pizza added to cart")
                }
            }, containerColor =  MaterialTheme.colorScheme.primary,

                ) {
                Icon(
                    imageVector = Icons.Filled.ShoppingCart,
                    contentDescription = "Add to cart"
                )
            }
        }) {
            innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = pizza.image),
                contentDescription = pizza.name,
                modifier = Modifier
                    .size(400.dp)
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.size(16.dp))
            Text(text = pizza.name, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.size(16.dp))
            Text(text = pizza.price.toString() + " €", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.size(16.dp))
            Text(text = "Extra cheese: ${extraCheese.value}g")
            Slider(
                value = extraCheese.value.toFloat(),
                onValueChange = { extraCheese.value = it.toInt() },
                valueRange = 0f..100f,

            )
        }
    }
}