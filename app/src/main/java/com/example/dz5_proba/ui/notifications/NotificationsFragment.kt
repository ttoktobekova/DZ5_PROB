package com.example.dz5_proba.ui.notifications

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import com.example.dz5_proba.data.FireBaseAuthManager
import com.example.dz5_proba.data.TaskManager
import com.example.dz5_proba.databinding.FragmentGalleryBinding
import com.example.dz5_proba.databinding.FragmentNotificationsBinding
import com.example.dz5_proba.ui.Gallery.Adapter.TaskAdapter
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



