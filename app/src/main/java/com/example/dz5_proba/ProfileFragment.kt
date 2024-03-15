package com.example.dz5_proba

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dz5_proba.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class ProfileFragment : Fragment() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var binding: FragmentProfileBinding
    private lateinit var userID: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Инициализация Firestore
        firestore = FirebaseFirestore.getInstance()

        // Получаем уникальный идентификатор пользователя
        userID = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        // Получаем имя пользователя из Firestore и устанавливаем его на экране профиля
        getUserDataFromFirestore()

        // Обработка нажатия на кнопку сохранения изменений
        binding.btnSaveChanges.setOnClickListener {
            val newUsername = binding.editTextNewUsername.text.toString()
            val newAvatarUrl = binding.editTextNewAvatarUrl.text.toString()
            updateProfileData(newUsername, newAvatarUrl)
        }
    }

    private fun getUserDataFromFirestore() {
        firestore.collection("user1").document(userID)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val username = documentSnapshot.getString("username")
                    binding.editTextCurrentUsername.setText(username)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("auth", "Ошибка не удалось войти в систему", exception.cause)
            }
    }

    private fun updateProfileData(newUsername: String, newAvatarUrl: String) {
        val userRef = firestore.collection("user1").document(userID)

        // Обновляем данные профиля в Firestore
        userRef
            .set(
                mapOf(
                    "username" to newUsername,
                    "avatarUrl" to newAvatarUrl
                ), SetOptions.merge()
            )
            .addOnSuccessListener {
                // Обработка успешного обновления данных профиля
                binding.editTextCurrentUsername.setText(newUsername)
            }
            .addOnFailureListener { exception ->
                Log.e("auth", "Ошибка не удалось войти в систему", exception.cause)
            }
    }
}
