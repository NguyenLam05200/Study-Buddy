package com.example.studybuddy.ui.todolist

import android.app.AlertDialog
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studybuddy.R
import com.example.studybuddy.TaskViewModelFactory
import com.example.studybuddy.data.local.DatabaseProvider
import com.example.studybuddy.ui.todolist.data.TaskModel
import com.example.studybuddy.ui.todolist.data.TaskAdapter
import com.example.studybuddy.ui.todolist.data.TaskRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton


class TaskFragment : Fragment() {
    lateinit var task_recyclerview: RecyclerView
    lateinit var task_adapter: TaskAdapter

    val viewModel: TaskViewModel by viewModels {
        val realm = DatabaseProvider.getDatabase()
        TaskViewModelFactory(TaskRepository(realm))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* Get from XML */
        getUIBindings(view)

        /* Recycler view data & adapter */
        task_adapter = TaskAdapter(emptyList())
        task_recyclerview.adapter = task_adapter
        task_recyclerview.layoutManager = LinearLayoutManager(requireContext())

        /* Get from database */
        viewModel.initialize()

        // Observe LiveData from ViewModel
        viewModel.tasks.observe(viewLifecycleOwner, Observer { tasks ->
            task_adapter.setTasks(tasks)
        })

        /* Actions */
        // Swipe right to delete
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition

                val task = viewModel.getTask(pos)
                task?.let {
                    Log.d("TODOLIST", "Deleting index ${pos}: ${task.text}, isChecked: ${task.isChecked}")

                    viewModel.remove(task)
                }
            }
        })
        itemTouchHelper.attachToRecyclerView(task_recyclerview)

        // Long press
        task_adapter.onTaskChecked = ::onTaskChecked
        task_adapter.onTaskLongClicked = ::onTaskLongClicked
    }

    override fun onStart() {
        super.onStart()

        val floating_button = requireActivity().findViewById<FloatingActionButton>(R.id.fab)
        floating_button.show()
        floating_button.setOnClickListener { onAddButtonClicked() }
    }

    override fun onResume() {
        super.onResume()

        val floating_button = requireActivity().findViewById<FloatingActionButton>(R.id.fab)
        floating_button.show()
        floating_button.setOnClickListener { onAddButtonClicked() }
    }

    private fun onAddButtonClicked() {
        val task = TaskModel()
        Log.d("TODOLIST", "Adding in fragment: ${task.uuid}, ${task.text}, ${task.isChecked}")

        viewModel.add(task)
    }

    private fun getUIBindings(view: View) {
        task_recyclerview = view.findViewById(R.id.task_recyclerview)
    }

    private fun onTaskChecked(pos: Int, isChecked: Boolean) {
        Log.d("_____TEST isChecked", isChecked.toString())
        val task = viewModel.tasks.value?.get(pos)
        task?.let {
            Log.d("TODOLIST", "isChecked: ${isChecked}")
            Log.d("TODOLIST", "Editing index ${pos}: ${task.uuid}, ${task.text}, isChecked: ${task.isChecked}")

            viewModel.update(pos, task.uuid, TaskModel(task.text, isChecked = isChecked))
        }
    }

    private fun onTaskLongClicked(pos: Int) {
        val task = viewModel.getTask(pos)
        task?.let {
            Log.d("TODOLIST", "Before at index ${pos}: ${task.uuid}, ${task.text}, ${task.isChecked}")

            // Tạo EditText với layout params
            val editText = EditText(requireContext()).apply {
                setText(task.text) // Set text hiện tại của task
                layoutParams = ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(40, 20, 40, 20) // Set margin cho EditText (left, top, right, bottom)
                }
            }

            // Tạo container để bọc EditText
            val container = FrameLayout(requireContext()).apply {
                addView(editText) // Thêm EditText vào container
            }

            // Tạo dialog
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Edit Task")
            builder.setView(container)

            builder.setPositiveButton("Save") { _, _ ->
                val new_text = editText.text.toString()
                Log.d("TODOLIST", "Changed text: ${task.text}")

                viewModel.update(pos, task.uuid, TaskModel(new_text, task.isChecked))
            }
            builder.setNegativeButton("Cancel", null)

            builder.show()
        }
    }

}