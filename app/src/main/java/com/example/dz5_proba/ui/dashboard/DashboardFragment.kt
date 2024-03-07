package com.example.dz5_proba.ui.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dz5_proba.databinding.FragmentDashboardBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val auth = Firebase.auth


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        upDataUi()
    }

    @SuppressLint("SetTextI18n")
    private fun upDataUi() {
binding.tvHello.text = "Hello ${auth.currentUser?.displayName}"    }





















    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}