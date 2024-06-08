package com.team4studio.travelnow.viewmodel

import android.app.Application
import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.util.toRange
import androidx.lifecycle.AndroidViewModel
import com.team4studio.travelnow.model.remote.entity.OrderItem
import com.team4studio.travelnow.model.repository.ProductRepository
import com.team4studio.travelnow.model.remote.entity.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.Normalizer
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.regex.Pattern
import kotlin.math.roundToInt

class FilterViewModel(val context: Application) : AndroidViewModel(context) {
    private val productRepository = ProductRepository
    private var filteredProductList = emptyList<Product>()!!

    var startPriceRange by mutableStateOf(0f)
    var endPriceRange by mutableStateOf(5000f)
    private var priceSliderRange by mutableStateOf(0f..5000f)
    var sliderEnd by mutableStateOf(5f)
    var onFinishedPriceRange by mutableStateOf(0f..5000f)
    var startPriceText by mutableStateOf("")
    var endPriceText by mutableStateOf("")

    private var currentCid = mutableStateOf("-1")
    var reviewSlider by mutableStateOf(5f)
    var steps by mutableStateOf(0)
    var reviewRange by mutableStateOf(0f..5f)

    var nameLocation by mutableStateOf("")

    var showDateCheckIn by mutableStateOf(false)
    var selectedDateCheckIn by mutableStateOf("")

    var showDateCheckOut by mutableStateOf(false)
    var selectedDateCheckOut by mutableStateOf("")

    var numberOfGuests by mutableStateOf("")
    var numberOfRooms by mutableStateOf("")

    fun setCurrentIndex(index: String) {
        currentCid.value = index
    }

    fun isIndexMatching(cid: String): Boolean {
        if (currentCid.value == cid) return true
        return false
    }


    fun setPriceEnd(range: ClosedFloatingPointRange<Float>) {
        onFinishedPriceRange = range
        setStartPrice(range.toRange().lower)
        setEndPrice(range.toRange().upper)
    }


    fun setPriceRange(range: ClosedFloatingPointRange<Float>) {
        priceSliderRange = range

    }

    fun getPriceRange(): ClosedFloatingPointRange<Float> {
        return priceSliderRange
    }

    fun setStartPrice(value: Float) {
        startPriceRange = value
    }

    fun getStartPrice(): Float {
        return startPriceRange
    }

    fun setEndPrice(value: Float) {
        endPriceRange = value
    }

    fun getEndPrice(): Float {
        return endPriceRange
    }

    fun setReviewSliderValue(value: Float) {
        reviewSlider = value
        sliderEnd = value
    }

    fun getReviewSliderValue(): Float {
        return reviewSlider
    }


    fun getCategoryProducts(
    ): List<Product>? {
        var products: List<Product>? = emptyList<Product>()

        runBlocking {
            this.launch(Dispatchers.IO) {
                products = productRepository.getCategoryProducts(
                    currentCid.value, getStartPrice(), getEndPrice()

                )
            }
        }
        Log.d("s price", "${getStartPrice()}")
        Log.d("e price", "${getEndPrice()}")
        Log.d("hello", "$products")
        Log.d("hello", "im here")
        return if (products?.isEmpty() != true) getMatchingProductsByRating(products!!) else emptyList()
    }

    fun getMatchingProductsByRating(products: List<Product>): MutableList<Product>? {
        val matchingProducts: MutableList<Product>? = emptyList<Product>().toMutableList()
        for (product in products) {
            var avgReviews = 0f
            if (product.reviews.isEmpty()) {
                avgReviews += 0f
            } else {
                for (reviews in product.reviews) {
                    avgReviews += reviews.stars
                }
            }

            if (avgReviews != 0f && avgReviews / (product.reviews.size.toFloat()) >= sliderEnd.roundToInt())
                matchingProducts?.add(
                    product
                )
            else if (avgReviews == 0f && avgReviews >= sliderEnd.roundToInt()) {
                Log.d("hello inside avg", "$avgReviews")
                matchingProducts?.add(
                    product
                )
            }
        }
        Log.d("avg", "${matchingProducts}")
        return matchingProducts
    }

    fun getNewArrivalProducts(
    ): List<Product>? {
        val products = filterProducts()

        return products
    }

    fun getTopRankedProducts(
    ): List<Product> {
        val matchingProducts = emptyList<Product>().toMutableList()
        var products = emptyList<Product>()
        runBlocking {
            this.launch(Dispatchers.IO) {
                products = productRepository.getNormalFilterQuery(
                    getStartPrice(), getEndPrice()
                )!!
            }
        }
        var avgReviews = 0f
        for (product in products)
            for (reviews in product.reviews) {
                avgReviews += reviews.stars

                if (avgReviews / (product.reviews.size.toFloat()) >= sliderEnd.roundToInt()) matchingProducts?.add(
                    product
                )
            }

        return matchingProducts ?: emptyList()
    }

    fun filterProducts(): List<Product>? {
        var products: List<Product>? = emptyList()
        runBlocking {
            this.launch(Dispatchers.IO) {
                products = productRepository.getNormalFilterQuery(
                    getStartPrice(), getEndPrice()
                )
            }
        }

        return if (products?.isEmpty() == false) getMatchingProductsByRating(products!!) else emptyList()
    }

    fun getTrendingProducts(
        orderItems: List<OrderItem>
//        orderCount: Int = 1,
//        qtyLowerBound: Int = 1,
//        qtyUpperBound: Int = 4,
//        queryType: String = "*",
    ): List<Product>? {
        val products = filterProducts()
        if (products.isNullOrEmpty()) {
            return null
        }
        val matchingProducts: MutableList<Product>? = emptyList<Product>().toMutableList()

        for (product in products) {
            for (orderItem in orderItems) {
                if (product.id == orderItem.pid) {

                    if (orderItem.quantity >= 5) {
                        matchingProducts?.add(product)
                        break;
                    }
                }
            }
        }
        return if (matchingProducts?.isEmpty() == false) getMatchingProductsByRating(
            matchingProducts!!
        ) else emptyList()
    }

    fun getSearchedProducts(
        title: String,
        products: List<Product>
    ): List<Product>? {
        val name = title.split(" ")
        if (products.isNullOrEmpty()) {
            return emptyList();
        }
        val filteredProducts = products.filter { it.title.contains(name[name.size - 1]) }
//        val matchingProducts: MutableList<Product>? = emptyList<Product>().toMutableList()
//        for (product in products!!) {
//            if (product.title.contains()) {
//                matchingProducts?.add(product)
//            }
//        }
        return if (filteredProducts?.isEmpty() != true) getMatchingProductsByRating(products!!) else null
        //return matchingProducts
    }


    fun showDatePickerDialog(
        context: Context,
        showDialog: Boolean,
        onDateSelected: (String) -> Unit
    ) {
        val format = SimpleDateFormat("EEEE, d MMMM yyyy", Locale("en", "VN"))

        if (showDialog) {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    val selectedCalendar = Calendar.getInstance().apply {
                        set(Calendar.YEAR, year)
                        set(Calendar.MONTH, month)
                        set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    }
                    onDateSelected(format.format(selectedCalendar.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }



}


