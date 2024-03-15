package com.example.dz5_proba.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.example.dz5_proba.data.CategoryManager
import com.example.dz5_proba.data.TaskManager
import com.example.dz5_proba.databinding.FragmentCreateTaskBinding
import com.example.dz5_proba.model.Task as Task

class CreateTaskFragment : Fragment() {
    private lateinit var binding: FragmentCreateTaskBinding
    private val taskManager = TaskManager()
    private val categoryManager = CategoryManager()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateTaskBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            btnCreate.setOnClickListener {
                val categoryTitle = binding.etCategoryName.text.toString()
                categoryManager.findCategoryByName(categoryTitle) { category ->
                    val task = Task(
                        title = binding.etTasksTitle.text.toString(),
                        categoryId = category.id)
                    taskManager.addToDB(task) {
                        setFragmentResult(TASK_RESULT_KEY, bundleOf())
                        findNavController().navigateUp()
                    }
                }
            }
        }
    }
    companion object {
        const val TASK_RESULT_KEY = "task result key"
    }


}