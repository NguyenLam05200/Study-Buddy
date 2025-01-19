package com.example.studybuddy.ui.todolist.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studybuddy.R

class TaskAdapter(private var tasks: List<Task>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    lateinit var onTaskChecked: ((Int, Boolean) -> Unit)
    lateinit var onTaskLongClicked: ((Int) -> Unit)

    fun setTasks(newTasks: List<Task>) {
        tasks = newTasks
        notifyDataSetChanged()
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val taskText: TextView = itemView.findViewById(R.id.task_textview)
        private val taskCheckbox: CheckBox = itemView.findViewById(R.id.task_checkbox)

        fun bind(task: Task, position: Int) {
            taskText.text = task.text

            // When not disabled setOnCheckedChangeListener() before changing frontend (XML):
            //      Firstly: changing the checkbox frontend (XML) will trigger setOnCheckedChangeListener()
            //      Secondly: onTaskChecked() will call adapter.notifyItemChanged(pos) which calls onBindViewHolder() which calls bind()
            //          => another setOnCheckedChangeListener() call
            taskCheckbox.setOnCheckedChangeListener(null)
            taskCheckbox.isChecked = task.isChecked

            // clicking the row
            itemView.setOnClickListener {
                onTaskChecked?.invoke(position, !taskCheckbox.isChecked)
            }

            // clicking the checkbox
            taskCheckbox.setOnCheckedChangeListener { _, isChecked ->
                onTaskChecked?.invoke(position, isChecked)
            }

            // long pressing for editing the task text
            itemView.setOnLongClickListener {
                onTaskLongClicked?.invoke(position)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position], position)
    }

    override fun getItemCount(): Int = tasks.size
}