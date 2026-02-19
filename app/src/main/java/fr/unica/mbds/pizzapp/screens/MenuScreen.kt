package fr.unica.mbds.pizzapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import fr.unica.mbds.pizzapp.PizzaRoute
import fr.unica.mbds.pizzapp.R
import fr.unica.mbds.pizzapp.data.DataSource
import fr.unica.mbds.pizzapp.model.Pizza
import fr.unica.mbds.pizzapp.ui.theme.PizzAppTheme

@Preview(showBackground = true)
@Composable
fun PizzaCardPreview() {
    PizzAppTheme {
        PizzaCard(pizza = Pizza("Capricciosa",12.5, image = R.drawable.pizza1))
    }
}
@Preview(showBackground = true)
@Composable
fun PizzaMenuPreview() {
    PizzAppTheme {
        PizzaMenu(DataSource().loadPizzas(), modifier = Modifier.fillMaxSize(),  navController = rememberNavController())
    }
}
@Composable
fun PizzaMenu(menu:List<Pizza>, modifier: Modifier = Modifier, navController: NavController){
    LazyColumn(modifier = modifier) {
        items(menu) { 
            PizzaCard(
                pizza = it,
                modifier = Modifier.padding(16.dp),
                onClickPizza = {
                    navController.navigate(
                        route = PizzaRoute(idPizza = menu.indexOf(it)))
                }
            )
        }
    }
}

@Composable
fun PizzaCard(pizza: Pizza, modifier: Modifier = Modifier, onClickPizza : () -> Unit = {}){
    Card(
        modifier=modifier.padding(1.dp),
        elevation =CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        onClick = onClickPizza,

    ) {
        Row (modifier = modifier.fillMaxWidth()
            .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                Text(
                    text=pizza.name,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier=modifier
                        .align(Alignment.Start),
                )
                Text(
                    text=pizza.price.toString()+" €",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier=modifier

                        .align(Alignment.Start)
                )
            }

            Image(
                painter = painterResource(id=pizza.image),
                contentDescription=pizza.name,
                modifier= Modifier
                    .padding(8.dp)
                    .size(100.dp)
            )


        }
    }
}