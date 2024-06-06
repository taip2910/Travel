package com.team4studio.travelnow.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.team4studio.travelnow.model.remote.entity.*
import com.team4studio.travelnow.model.repository.OrderRepository
import com.team4studio.travelnow.model.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class OrderDetailsViewModel(val context: Application) : AndroidViewModel(context) {
    private val orderRepository = OrderRepository
    private val productRepository = ProductRepository

    val orderItemProducts = mutableStateOf<List<FullOrderDetail>>(emptyList())

    var allOrderFullDetails = mutableListOf<OrderItem>()

    private val placeHolderOrder = Order("", "", "Invalid Product", 0.0, "", "all", emptyList())
    private val placeHolderProduct = Product(
        cid = "",
        title = "Missing product",
        price = 0.0,
        description = "",
        stock = 0,
        image = "",
        timeAdded = 0,
        reviews = mutableListOf<Review>()
    )

    fun getOrderById(order_id: String): Order {
        var order: Order? = null
        val tempOrderItemDetail = mutableListOf<FullOrderDetail>()

        runBlocking {
            this.launch(Dispatchers.IO) {
                order = orderRepository.getOrderById(order_id)
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            for (item in order!!.items) {
                val product = productRepository.getProductById(item.pid)
                product?.let {
                    tempOrderItemDetail.add(FullOrderDetail(item, product))
                } ?: run { tempOrderItemDetail.add(FullOrderDetail(item, placeHolderProduct)) }
            }
            orderItemProducts.value = tempOrderItemDetail
        }

        return order ?: placeHolderOrder
    }
}