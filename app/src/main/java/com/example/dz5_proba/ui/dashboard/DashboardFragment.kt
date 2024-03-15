package com.example.dz5_proba.ui.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.dz5_proba.data.CategoryManager
import com.example.dz5_proba.databinding.FragmentDashboardBinding
import com.example.dz5_proba.model.Category
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val auth = Firebase.auth
    private val categoryManager = CategoryManager()
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
        binding.btnAdd.setOnClickListener {
            val category = Category(title = binding.etTitle.text.toString())
            categoryManager.add(binding.etTitle.text.toString()) {
                Toast.makeText(requireContext(), "Create cotegory", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}