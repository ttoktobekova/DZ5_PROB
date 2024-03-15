package com.example.dz5_proba.data

import android.util.Log
import com.example.dz5_proba.Constants
import com.example.dz5_proba.model.Category
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class CategoryManager {
    private val auth = Firebase.auth
    private val firestore = Firebase.firestore
    private val failruListener: (Exception) -> Unit = {
        Log.e("firebace", "add task failed", it)
    }

    fun findCategoryByName(title: String, success: (Category) -> Unit) {
        getAll { categories ->
            categories.forEach {
                if (it.title == title) success(it)
            }
        }
    }

    fun add(title: String, success: () -> Unit) {
        val category = Category(
            title = title, authorId = auth.currentUser?.uid.toString()
        )
        firestore
            .collection(Constants.Firebace.CATEGORIES)
            .add(category)
            .addOnFailureListener(failruListener)
            .addOnSuccessListener {
                it.set(category.copy(id = it.id))
                success()
            }
    }

    fun getAll(onSuccess: (List<Category>) -> Unit) {
        firestore
            .collection(Constants.Firebace.CATEGORIES)
            .get()
            .addOnFailureListener(failruListener)
            .addOnSuccessListener {
                onSuccess(it.toObjects(Category::class.java))
            }
    }
}