package com.example.dz5_proba.data

import android.util.Log
import com.example.dz5_proba.Constants
import com.example.dz5_proba.model.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class TaskManager {
    private val firestore = Firebase.firestore
    private val auth = Firebase.auth
    private val failruListener: (Exception) -> Unit = {
        Log.e("firebace", "add task failed", it)
    }
    fun addToDB(
        task: Task = generateRandomTask(), onSuccess: () -> Unit) {
        firestore.collection(Constants.Firebace.Users)
            .document(auth.currentUser?.uid.toString())
            .collection(Constants.Firebace.Tasks)
            .add(task)
            .addOnSuccessListener {
                it.set(task.copy(id = it.id))
                onSuccess() }
            .addOnFailureListener {
                Log.e("firebase", "add task failed", it) }
    }

    fun updateTask(task: Task) {
        firestore
            .collection(Constants.Firebace.Users)
            .document(auth.currentUser?.uid.toString())
            .collection(Constants.Firebace.Tasks)
            .document(task.id)
            .set(task)
            .addOnFailureListener(failruListener)
    }
    fun getAllTasks(onSuccess: (List<Task>) -> Unit) {
        firestore.collection(Constants.Firebace.Users)
            .document(auth.currentUser?.uid.toString())
            .collection(Constants.Firebace.Tasks)
            .get()
            .addOnSuccessListener {
                onSuccess(it.toObjects(Task::class.java)) }
            .addOnFailureListener(failruListener) }

    fun generateRandomTask() = Task(
        title = "Title(${kotlin.random.Random.nextInt(1000)}",
        done = false)
}

