package fr.unica.mbds.pizzapp.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import fr.unica.mbds.pizzapp.data.DataSource
import fr.unica.mbds.pizzapp.model.Pizza

class PizzaViewModel : ViewModel() {
    private val _pizzas = mutableStateListOf<Pizza>()
    val pizzas: List<Pizza> get() = _pizzas

    init {
        _pizzas.addAll(DataSource().loadPizzas())
    }

    fun getPizzaById(id: Int): Pizza {
        return _pizzas[id]
    }


    private val _cart = mutableStateListOf<Triple<Pizza, Int, Int>>()
    val cart: List<Triple<Pizza, Int, Int>> get() = _cart

    private val _orderSummary = mutableStateOf("Résumé de la commande non disponible.")
    val orderSummary: String get() = _orderSummary.value

    fun loadPizza(id: Int): Pizza? = _pizzas.getOrNull(id)

    fun addToCart(pizza: Pizza, quantity: Int = 1, cheeseQuantity: Int = 0) {
        val existingItem = _cart.firstOrNull { it.first == pizza }
        if (existingItem != null) {
            _cart.remove(existingItem)
            _cart.add(Triple(existingItem.first, existingItem.second + quantity, existingItem.third + cheeseQuantity))
        } else {
            _cart.add(Triple(pizza, quantity, cheeseQuantity))
        }
    }

    fun updateCart(pizza: Pizza, newQuantity: Int = 1, newCheeseQuantity: Int) {
        _cart.removeIf { it.first == pizza }
        if (newQuantity > 0) {
            _cart.add(Triple(pizza, newQuantity, newCheeseQuantity))
        }
    }

    fun removeFromCart(pizza: Pizza) {
        _cart.removeIf { it.first == pizza }
    }

    fun clearCart() {
        _cart.clear()
    }

    fun getTotalPrice(): Double {
        return _cart.sumOf { (pizza, quantity, cheeseQuantity) ->
            (pizza.price * quantity) + (cheeseQuantity * 1.0)
        }
    }

    fun validateOrder(): Boolean {
        return _cart.isNotEmpty()
    }

    fun checkout() {
        clearCart()
    }

    fun updateCartFromResponse(newCart: List<Triple<Pizza, Int, Int>>, summary: String) {
        _cart.clear()
        _cart.addAll(newCart)
        _orderSummary.value = summary
    }



}