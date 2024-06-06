package com.team4studio.travelnow.model.repository

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import com.team4studio.travelnow.model.remote.entity.User
import kotlinx.coroutines.tasks.await

object UserRepository {
    val db by lazy { Firebase.firestore }
    private val userCollectionRef by lazy { db.collection("user") }

    init {
        val settings = firestoreSettings { isPersistenceEnabled = true }
        db.firestoreSettings = settings
    }

    suspend fun getUserById(userId: String): User? =
        userCollectionRef.document(userId).get().await().toObject(User::class.java)

    suspend fun getUserByEmail(email: String): User? {
        val users =
            userCollectionRef.whereEqualTo("email", email).get().await().toObjects(User::class.java)
        return if (users.isEmpty()) null
        else users[0]
    }


    fun updateUser(user: User) {
        userCollectionRef.document(user.id).set(user)
    }

    fun insertUser(user: User) {
        val documentId = userCollectionRef.document().id
        user.id = documentId
        userCollectionRef.document(documentId).set(user)
    }
}



