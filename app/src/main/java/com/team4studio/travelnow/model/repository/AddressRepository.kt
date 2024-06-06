package com.team4studio.travelnow.model.repository

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import com.team4studio.travelnow.model.remote.entity.Address
import kotlinx.coroutines.tasks.await

object AddressRepository {
    val db by lazy { Firebase.firestore }
    private val addressCollectionRef by lazy { db.collection("address") }

    init {
        val settings = firestoreSettings { isPersistenceEnabled = true }
        db.firestoreSettings = settings
    }

    suspend fun getAddressesByUser(userId: String): List<Address> =
        addressCollectionRef.whereEqualTo("uid", userId).get().await()
            .toObjects(Address::class.java)


    suspend fun getAddressById(addressId: String): Address? =
        addressCollectionRef.document(addressId).get().await().toObject(
            Address::class.java
        )

    fun insertAddress(address: Address) {
        val documentId = addressCollectionRef.document().id
        address.id = documentId
        addressCollectionRef.document(documentId).set(address)
    }

    fun updateAddress(address: Address) {
        addressCollectionRef.document(address.id).set(address)
    }

    suspend fun deleteAddress(address: Address) {
        addressCollectionRef.document(address.id).delete().await()
    }
}