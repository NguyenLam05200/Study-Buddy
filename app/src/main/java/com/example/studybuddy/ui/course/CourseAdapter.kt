package com.example.studybuddy.ui.course

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.studybuddy.R
import com.example.studybuddy.data.local.model.CourseModel

class CourseAdapter(
    private val onAction: ((CourseModel, Action) -> Unit)
) : ListAdapter<CourseModel, CourseAdapter.CourseViewHolder>(DIFF_CALLBACK) {

    enum class Action {
        EDIT, DELETE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_course, parent, false)
        return CourseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = getItem(position)
        holder.bind(course)
    }

    inner class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.textViewCourseName)
        private val timeTextView: TextView = itemView.findViewById(R.id.textViewCourseTime)
        private val dateTextView: TextView = itemView.findViewById(R.id.textViewCourseDate)
        private val editButton: ImageButton = itemView.findViewById(R.id.buttonEditCourse)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.buttonDeleteCourse)

        fun bind(course: CourseModel) {
            nameTextView.text = course.name
            timeTextView.text = course.formatTimeRange()
            dateTextView.text = course.formatDateRange()

            editButton.setOnClickListener { onAction(course, Action.EDIT) }
            deleteButton.setOnClickListener { onAction(course, Action.DELETE) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CourseModel>() {
            override fun areItemsTheSame(oldItem: CourseModel, newItem: CourseModel) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: CourseModel, newItem: CourseModel) =
                oldItem == newItem
        }
    }
}
