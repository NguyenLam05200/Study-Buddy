package com.example.studybuddy.ui.filemanager.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studybuddy.R
import com.example.studybuddy.utilities.LAYOUT_MODE

class FileAdapter(private var files: List<FileItem>) : RecyclerView.Adapter<FileAdapter.FileViewHolder>() {
    var layoutMode: LAYOUT_MODE = LAYOUT_MODE.LINEAR
    lateinit var onClick: (FileItem, Int) -> Unit
    lateinit var onLongClick: (FileItem, Int) -> Unit

    inner class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val fileIcon: ImageView = itemView.findViewById(R.id.file_icon)
        private val fileName: TextView = itemView.findViewById(R.id.file_display_name)

        fun bind(file: FileItem, pos: Int) {
            fileName.text = file.displayName

            // Choose icon based on file type
            fileIcon.setImageResource(getFileIcon(file))

            // Click to open the file with another app
            itemView.setOnClickListener { onClick?.invoke(file, pos) }

            // Long press to rename
            itemView.setOnLongClickListener {
                onLongClick?.invoke(file, pos)
                true
            }
        }

        private fun getFileIcon(file: FileItem): Int {
            return when {
                file.uri.endsWith(".pdf") -> R.drawable.ic_pdf
                (file.uri.endsWith(".doc") || file.uri.endsWith(".docx")) -> R.drawable.ic_word
                (file.uri.endsWith(".jpg") || file.uri.endsWith(".png")) -> R.drawable.ic_image
                else -> R.drawable.ic_file_placeholder
            }
        }
    }

    fun setFiles(newFiles: List<FileItem>) {
        files = newFiles
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        var view = layoutInflater.inflate(R.layout.item_file_linear, parent, false)
//        if (layoutMode.value == LAYOUT_MODE.LINEAR.value) {
//            view = layoutInflater.inflate(R.layout.item_file_linear, parent, false)
//        } else {
//            view = layoutInflater.inflate(R.layout.item_file_grid, parent, false)
//        }

        return FileViewHolder(view)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.bind(files[position], position)
    }

    override fun getItemCount(): Int = files.size
}