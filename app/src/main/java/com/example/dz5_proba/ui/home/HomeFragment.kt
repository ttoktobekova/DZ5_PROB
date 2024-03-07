package com.example.dz5_proba.ui.home

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dz5_proba.R
import com.example.dz5_proba.databinding.FragmentHomeBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    //для google  регистрации
    private val auth = Firebase.auth
    private val gso by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            //Ошибка! toString()
            .build()
    }
    val mGoogleSignInClienGoogle by lazy {
        GoogleSignIn.getClient(requireActivity(), gso)
    }
    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Обработка ошибки
            }

        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (auth.currentUser != null) {
            UpdataUIGoogle()
        }
        click()
        addListeners()
    }


    private fun addListeners() {
        //Войти в систему
        binding.btnSignIn.setOnClickListener {
            if (!validate()) return@setOnClickListener
            signIn(
                email = binding.etEmail.text.toString(),
                password = binding.etPassword.text.toString()
            )
        }
        //Регистрация
        binding.btnSignUp.setOnClickListener {
            if (!validate()) return@setOnClickListener

            signUp(
                email = binding.etEmail.text.toString(),
                password = binding.etPassword.text.toString()
            )
        }
        binding.tvForgotPassword.setOnClickListener {
            resetPassword()
        }
        binding.btnConfirm.setOnClickListener {
            confirmResetPassword()
        }
    }

    private fun confirmResetPassword() {
        val confirmCode = binding.etConfirum.text
        val newPassword = binding.etNewPassword.text
        if (confirmCode.isNullOrEmpty()) {
            binding.etConfirum.error = "Введите код подверждение"
            return
        }
        if (newPassword.isNullOrEmpty()) {
            binding.etNewPassword.error = "Введите новый пароль"
            return
        }
        auth.confirmPasswordReset(confirmCode.toString(), newPassword.toString())
            .addOnFailureListener {
                Log.e("auth", "не удалось войти в систему", it.cause)
                Toast.makeText(requireContext(), "Что та пошло не так", Toast.LENGTH_SHORT).show()
                hideResetPassword()
            }
            .addOnSuccessListener {
                hideResetPassword()
            }
    }

    private fun resetPassword() {
        val email = binding.etEmail.text
        if (email.isNullOrEmpty()) {
            binding.etEmail.error = "Введите почту"
            return
        }
        auth.sendPasswordResetEmail(email.toString(), ActionCodeSettings.zzb())
            .addOnFailureListener {
                Log.e("auth", "не удалось войти в систему", it.cause)


            }
            .addOnSuccessListener {
                showResetPassword()
                AlertDialog.Builder(requireContext())
                    .setTitle("Подвердите сброс пароля")
                    .setMessage("Мы отправили вам код подверждение на вашу учетную запись электронный почти! $email Введите код")
                    .show()
            }
    }

    private fun validate(): Boolean {
        if (binding.etEmail.text.isNullOrEmpty()) {
            binding.etEmail.error = "Введите почту"
            return false
        }
        if (binding.etPassword.text.isNullOrEmpty()) {
            binding.etPassword.error = "Введите пароль"
            return false
        }
        return true
    }

    private fun signIn(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnFailureListener {
                Log.e("auth", "SignIn failed", it.cause)
                Toast.makeText(
                    requireContext(),
                    "адрес электронной почты или пароль указаны неправильно",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnSuccessListener {
                Toast.makeText(
                    requireContext(),
                    "адрес электронной почты или пароль указаны ПРАВИЛЬНО ${it.user?.displayName ?: it.user?.email}",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().navigate(R.id.navigation_dashboard)
            }
    }

    private fun signUp(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnFailureListener {
                Log.e("auth", "SignUp failed", it.cause)
                Toast.makeText(
                    requireContext(),
                    "адрес электронной почты или пароль указаны неправильно",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnSuccessListener {
                Toast.makeText(
                    requireContext(),
                    "адрес электронной почты или пароль указаны ПРАВИЛЬНО ${it.user?.displayName ?: it.user?.email}",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().navigate(R.id.navigation_dashboard)

            }
    }

    private fun hideResetPassword() {
        binding.etConfirum.visibility = View.GONE
        binding.btnConfirm.visibility = View.GONE
        binding.etNewPassword.visibility = View.GONE
    }

    private fun showResetPassword() {
        binding.etConfirum.visibility = View.VISIBLE
        binding.btnConfirm.visibility = View.VISIBLE
        binding.etNewPassword.visibility = View.VISIBLE
    }

    /////
/////
/////
/////
/////
/////
/////
/////
/////
/////
/////
/////
/////
/////
// ///
    //МЕТОДЫ
    private fun click() {
        binding.btnGoogle.setOnClickListener {
            singInGoogle()
        }
    }

    private fun singInGoogle() {
        googleSignInLauncher.launch(mGoogleSignInClienGoogle.signInIntent)
    }

    private fun firebaseAuthWithGoogle(token: String) {
        val credential = GoogleAuthProvider.getCredential(token, null)
        auth.signInWithCredential(credential)
            .addOnFailureListener {
                Log.e("auth", "Ошибка не удалось войти в систему", it.cause)
            }
            .addOnSuccessListener {
                UpdataUIGoogle()
            }

    }


    private fun UpdataUIGoogle() {

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}