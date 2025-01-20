package com.example.studybuddy.ui.deadline

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.studybuddy.R
import com.example.studybuddy.data.local.model.DeadlineModel

class DeadlineAdapter(
    private val onAction: (DeadlineModel, Action) -> Unit,
    private val onItemClick: (DeadlineModel) -> Unit // Thêm lambda function cho click item
) : ListAdapter<DeadlineModel, DeadlineAdapter.DeadlineViewHolder>(DIFF_CALLBACK) {

    enum class Action {
        EDIT, DELETE
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeadlineViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_deadline, parent, false)
        return DeadlineViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeadlineViewHolder, position: Int) {
        val deadline = getItem(position)
        holder.bind(deadline)
    }

    inner class DeadlineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.textViewName)
        private val timeTextView: TextView = itemView.findViewById(R.id.textViewDeadlineTime)
        private val deadlineCourseNameTextView: TextView = itemView.findViewById(R.id.textViewDeadlineCourseName)
        private val editButton: ImageButton = itemView.findViewById(R.id.buttonEditDeadline)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.buttonDeleteDeadline)

        fun bind(deadline: DeadlineModel) {
            nameTextView.text = deadline.name
            timeTextView.text = deadline.formatTimeDeadlineTime()
            deadlineCourseNameTextView.text = deadline.courseName

            editButton.setOnClickListener { onAction(deadline, Action.EDIT) }
            deleteButton.setOnClickListener { onAction(deadline, Action.DELETE) }

            // Navigate when clicking the item
            itemView.setOnClickListener { onItemClick(deadline) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DeadlineModel>() {
            override fun areItemsTheSame(oldItem: DeadlineModel, newItem: DeadlineModel) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: DeadlineModel, newItem: DeadlineModel) = oldItem == newItem
        }
    }
}
