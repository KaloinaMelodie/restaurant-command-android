package fr.unica.mbds.pizzapp.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import fr.unica.mbds.pizzapp.model.Pizza

class CommandViewModel():ViewModel() {
    private val _commande = mutableStateListOf<Pair<Pizza, Int>>()  // Pair<Pizza, quantitéFromage>
    val commande: List<Pair<Pizza, Int>> get() = _commande

    //addtocart
    fun ajouterPizzaCommande(pizza: Pizza, quantiteFromage: Int) {
        _commande.add(pizza to quantiteFromage)
    }
    // Retirer une pizza de la commande
    // removefromcart
    fun EffacerPizzaCommande(pizza: Pizza,quantiteFromage: Int) {
        _commande.remove(Pair(pizza, quantiteFromage))
    }

    // Modifier la quantité de fromage extra pour une pizza
    //updatecart
    fun updatePizzaCheese(pizza: Pizza,oldExtraCheese:Int, extraCheese: Int) {
        val index = _commande.indexOf(Pair(pizza,oldExtraCheese))
        if (index != -1) {
            _commande[index] = pizza to extraCheese
        }
    }

    // Vider la commande
    //clearcart
    fun clearOrder() {
        _commande.clear()
    }

}