package fr.unica.mbds.pizzapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import fr.unica.mbds.pizzapp.screens.HomeScreen
import fr.unica.mbds.pizzapp.screens.PanierScreen
import fr.unica.mbds.pizzapp.screens.PizzaMenu
import fr.unica.mbds.pizzapp.screens.PizzaDetail
import fr.unica.mbds.pizzapp.viewModel.ChatViewModel
import fr.unica.mbds.pizzapp.viewModel.CommandViewModel
import fr.unica.mbds.pizzapp.viewModel.PizzaViewModel


@kotlinx.serialization.Serializable
object PizzaList

@kotlinx.serialization.Serializable
data class PizzaRoute(val idPizza:Int)

@kotlinx.serialization.Serializable
object Accueil

@kotlinx.serialization.Serializable
object Panier


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(pizzaViewModel: PizzaViewModel = viewModel<PizzaViewModel>(),chatViewModel: ChatViewModel=viewModel<ChatViewModel>()) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold (
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("PizzApp") }
            )
        },
        bottomBar = {
            BottomAppBar {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(onClick = { navController.navigate(Accueil) }
                    ) {
                        Icon(Icons.Default.Home, contentDescription = "Menu")
                    }
                    IconButton(onClick = { navController.navigate(PizzaList) }) {
                        Icon(Icons.AutoMirrored.Filled.List, contentDescription = "List")
                    }
                    IconButton(onClick = { navController.navigate(Panier) }) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Panier")
                    }
                }
            }

        }

    ){
        innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Accueil,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<Accueil> {
                HomeScreen(navController = navController, pizzaViewModel= pizzaViewModel, chatViewModel = chatViewModel)
            }
            composable<Panier> {
                PanierScreen(pizzaViewModel=pizzaViewModel, navController = navController)
            }
            composable<PizzaList>{
                PizzaMenu(
                    menu =pizzaViewModel.pizzas,
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    navController = navController,
                )
            }
            composable<PizzaRoute> { backStackEntry ->
                val pizzaRoute = backStackEntry.toRoute<PizzaRoute>()

                PizzaDetail(
                    pizzaViewModel = pizzaViewModel,
                    pizza = pizzaViewModel.getPizzaById(pizzaRoute.idPizza),
                    snackbarHostState = snackbarHostState
                )
            }
        }

    }


}