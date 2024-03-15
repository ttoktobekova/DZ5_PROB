package com.example.dz5_proba.data

import com.example.dz5_proba.Constants
import com.example.dz5_proba.model.Category
import com.example.dz5_proba.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class FireBaseAuthManager {
    private val auth = Firebase.auth
    private val firestore = Firebase.firestore

    fun userSignedUp() {
        val user = User(
            name = auth.currentUser?.displayName.toString()
        )
        fun userSignedUp() {
            val user = User(
                name = auth.currentUser?.displayName.toString()
            )
            firestore
                .collection(Constants.Firebace.Users)
                .document(auth.currentUser?.uid.toString())
                .set(user)
        }
    }
}