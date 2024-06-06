package com.team4studio.travelnow.model.repository

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import com.team4studio.travelnow.model.remote.entity.CartWithProduct
import com.team4studio.travelnow.model.remote.entity.Cart
import com.team4studio.travelnow.model.remote.entity.Product
import kotlinx.coroutines.tasks.await

object CartRepository {
    val db by lazy { Firebase.firestore }
    private val cartCollectionRef by lazy { db.collection("cart") }
    private val productCollectionRef by lazy { db.collection("product") }

    init {
        val settings = firestoreSettings { isPersistenceEnabled = true }
        db.firestoreSettings = settings
    }

    suspend fun getCartByUser(userId: String): List<Cart> =
        cartCollectionRef.whereEqualTo("uid", userId).get().await().toObjects(
            Cart::class.java
        )

    suspend fun getCartWithProductByUser(userId: String): List<CartWithProduct> {
        val cartWithProductList = mutableListOf<CartWithProduct>()

        val cart = getCartByUser(userId)
        for (cartItem in cart) {
            val product = productCollectionRef.document(cartItem.pid).get().await().toObject(
                Product::class.java
            )

            product?.let {
                cartWithProductList.add(CartWithProduct(cartItem, product))
            }
        }
        return cartWithProductList
    }

    suspend fun insertCart(cart: Cart) {
        //Check if item already in user cart
        val userCart = getCartByUser(cart.uid)
        for (item in userCart) {
            if (item.pid == cart.pid) {
                // Update quantity if item already in cart
                item.quantity += 1
                cartCollectionRef.document(item.id).set(item)
                return
            }
        }

        // If not already in cart, add new document
        val documentId = cartCollectionRef.document().id
        cart.id = documentId
        cartCollectionRef.document(documentId).set(cart)
    }

    suspend fun deleteCart(cart: Cart) {
        cartCollectionRef.document(cart.id).delete().await()
    }

    suspend fun updateQuantity(userId: String, productId: String, quantity: Int) {
        val queryResult =
            cartCollectionRef.whereEqualTo("uid", userId).whereEqualTo("pid", productId).get()
                .await().toObjects(
                    Cart::class.java
                )
        val cart = queryResult[0]
        cart.quantity += quantity
        cartCollectionRef.document(cart.id).set(cart)
    }

    suspend fun deleteCartByUser(userId: String) {
        val userCart = getCartByUser(userId)
        for (cartItem in userCart) {
            cartCollectionRef.document(cartItem.id).delete().await()
        }
    }
}