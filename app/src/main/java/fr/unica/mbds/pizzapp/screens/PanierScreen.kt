package fr.unica.mbds.pizzapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.room.util.query
import fr.unica.mbds.pizzapp.Accueil
import fr.unica.mbds.pizzapp.PizzaList
import fr.unica.mbds.pizzapp.model.Pizza
import fr.unica.mbds.pizzapp.viewModel.CommandViewModel
import fr.unica.mbds.pizzapp.viewModel.PizzaViewModel

@Composable
fun PanierScreen(pizzaViewModel: PizzaViewModel, navController: NavController) {
    val commandes=pizzaViewModel.cart
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(text = "Mon panier", style = MaterialTheme.typography.headlineLarge)
        if(commandes.isNotEmpty()){
            Button (onClick = { pizzaViewModel.clearCart()
            navController.navigate(Accueil)
            },
                modifier = Modifier.padding(16.dp)
            ,
                colors = ButtonDefaults.buttonColors(
                    Color(0xFF5F8D37)
                )) {
                Text("Payer la commande")
            }
        }

        Column(
            modifier = Modifier.padding(16.dp).fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            if (commandes.isEmpty()) {
                Text("Ton panier est vide!", modifier = Modifier.padding(16.dp))
                Button (onClick = {
                    navController.navigate(PizzaList)
                },
                    modifier = Modifier.padding(16.dp)

                    ) {
                    Text("Voir le menu")
                }
            } else {
                LazyColumn  {
                    items(commandes) { (pizza, qt,extraCheese) ->
                        val existingItem = pizzaViewModel.cart.firstOrNull { it.first == pizza }
                        var quantity by remember { mutableStateOf(existingItem?.second ?: 1) }
                        var cheeseQuantity by remember { mutableStateOf(existingItem?.third ?: 0) }

                        CommandeItem(
                            pizza = pizza,
                            quantite = quantity,
                            extraCheese = extraCheese,
                            onRemove = {
                                pizzaViewModel.removeFromCart(pizza)
//                                pizzaViewModel.EffacerPizzaCommande(pizza, extraCheese)
                                       },
                            onUpdateCheese = { newCheese ->
                                pizzaViewModel.updateCart(pizza = pizza, newQuantity = quantity, newCheeseQuantity = newCheese)
//                                pizzaViewModel.updatePizzaCheese(pizza, extraCheese, newCheese)

                            }
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    }



                }
                val totalPrice = pizzaViewModel.getTotalPrice()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Total : $totalPrice €", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
@Composable
fun CommandeItem(
    pizza: Pizza,
    extraCheese: Int,
    quantite: Int,
    onRemove: () -> Unit,
    onUpdateCheese: (Int) -> Unit
) {
    var cheese = remember{ mutableStateOf(extraCheese) }

    Column(modifier = Modifier.padding(8.dp)) {
        Row (modifier = Modifier.fillMaxWidth()
            .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Column {
                Text("${quantite} ${pizza.name} - ${(pizza.price * quantite) + (extraCheese * 1.0)} €")
                Text("Fromage : ${cheese.value}g")
            }
            Image(
                painter = painterResource(id=pizza.image),
                contentDescription=pizza.name,
                modifier= Modifier
                    .padding(8.dp)
                    .size(100.dp)
            )
        }


        Slider(
            value = cheese.value.toFloat(),
            onValueChange = { cheese.value = it.toInt() },
            valueRange = 0f..100f,
            onValueChangeFinished = {
                onUpdateCheese(cheese.value)
            },


        )

        Row   {
            Button(onClick = onRemove) {
                Text("Supprimer")
            }
        }
    }
}
