package com.example.studybuddy.ui.todolist

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studybuddy.R
import com.example.studybuddy.TaskViewModelFactory
import com.example.studybuddy.data.local.DatabaseProvider
import com.example.studybuddy.data.local.model.Task
import com.example.studybuddy.data.repository.TaskRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
class TaskFragment : Fragment() {
    private lateinit var taskRecyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var viewModel: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val repository = TaskRepository(DatabaseProvider.getDatabase())
        val factory = TaskViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[TaskViewModel::class.java]

        return inflater.inflate(R.layout.fragment_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Bind RecyclerView
        taskRecyclerView = view.findViewById(R.id.task_recyclerview)
        taskAdapter = TaskAdapter(
            tasks = emptyList(),
            onTaskChecked = ::onTaskChecked,
            onTaskLongClicked = ::onTaskLongClicked
        )
        taskRecyclerView.adapter = taskAdapter
        taskRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Call initialize to load tasks
        viewModel.initialize()

        // Observe LiveData
        viewModel.tasks.observe(viewLifecycleOwner) { tasks ->
            taskAdapter.setTasks(tasks) // Automatically updates RecyclerView
        }

        // Swipe to delete
        setupSwipeToDelete()
    }

    override fun onStart() {
        super.onStart()
        setupFloatingActionButton()
    }

    private fun setupFloatingActionButton() {
        val floatingButton = requireActivity().findViewById<FloatingActionButton>(R.id.fab)
        floatingButton?.let {
            it.setOnClickListener { onAddButtonClicked() }
        } ?: Log.e("TaskFragment", "FloatingActionButton not found!")
    }

    private fun setupSwipeToDelete() {
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                val task = viewModel.getTask(pos)
                task?.let {
                    viewModel.remove(task)
                }
            }
        })
        itemTouchHelper.attachToRecyclerView(taskRecyclerView)
    }

    private fun onAddButtonClicked() {
        val newTask = Task()
        viewModel.add(newTask)
    }

    private fun onTaskChecked(pos: Int, isChecked: Boolean) {
        val task = viewModel.tasks.value?.get(pos)
        task?.let {
            viewModel.update(pos, task.uuid, Task(task.text, isChecked = isChecked))
        }
    }

    private fun onTaskLongClicked(pos: Int) {
        val editText = EditText(requireContext())
        val task = viewModel.getTask(pos)
        task?.let {
            editText.setText(task.text)

            AlertDialog.Builder(requireContext())
                .setTitle("Edit Task")
                .setView(editText)
                .setPositiveButton("Save") { _, _ ->
                    val updatedText = editText.text.toString()
                    viewModel.update(pos, task.uuid, Task(updatedText, task.isChecked))
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}
