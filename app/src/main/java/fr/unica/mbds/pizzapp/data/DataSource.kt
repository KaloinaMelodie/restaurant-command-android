package fr.unica.mbds.pizzapp.data

import fr.unica.mbds.pizzapp.R
import fr.unica.mbds.pizzapp.model.Pizza

class DataSource {
    fun loadPizzas():List<Pizza>{
        return listOf(
            Pizza("Margherita",8.0,R.drawable.pizza1),
            Pizza("Capricciosa",10.0,R.drawable.pizza2),
            Pizza("Diavola",9.0,R.drawable.pizza3),
            Pizza("Quattro Stagioni",11.0,R.drawable.pizza4),
            Pizza("Quattro Fromaggi",12.0,R.drawable.pizza5),
            Pizza("Marinara",7.0,R.drawable.pizza6),
            Pizza("Pepperoni",9.0,R.drawable.pizza7),
            Pizza("Proscuito",10.0,R.drawable.pizza8),
            Pizza("Frutti di mare",13.0,R.drawable.pizza9)
        )
    }
    fun loadPizza(id: Int): Pizza {
        return loadPizzas()[id]
    }
}