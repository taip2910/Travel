package com.team4studio.travelnow.model.remote.repo

import com.team4studio.travelnow.model.remote.api.EmerceApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TravelNowRepo {
    private const val BASE_URL =
        "https://gist.githubusercontent.com/hani-hj1908619/437ddd8aa6fb9c2c7dd5643f5f3a72aa/raw/0074162f7995f2897503c194dfb451ad5b2d798a/"

    private val travelnowApi: EmerceApi by lazy {
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .build().create(EmerceApi::class.java)
    }

    suspend fun getUsers() = travelnowApi.getUsers()
    suspend fun getCategories() = travelnowApi.getCategories()
    suspend fun getProducts() = travelnowApi.getProducts()
}