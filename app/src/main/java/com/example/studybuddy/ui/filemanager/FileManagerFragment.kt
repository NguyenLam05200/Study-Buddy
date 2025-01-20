package com.example.studybuddy.ui.filemanager

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studybuddy.R
import com.example.studybuddy.ui.filemanager.data.FileAdapter
import com.example.studybuddy.ui.filemanager.data.FileItem
import com.example.studybuddy.utilities.CONF
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID

class FileManagerFragment : Fragment() {
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: FileAdapter
    lateinit var files: MutableList<FileItem>

    private val viewModel: FileManagerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_file_manager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* XML Bindings */
        getUIBindings(view)

        /* */
        files = mutableListOf<FileItem>(
            FileItem(
                "content://com.android.providers.downloads.documents/document/raw:/storage/emulated/0/Download/XLamp-XHP50.3.pdf",
                "PDF Test"
            ),
            FileItem(
                "content://com.android.providers.downloads.documents/document/raw:/storage/emulated/0/Download/starecustom.png",
                "Image Test"
            ),
            FileItem(
                "content://com.android.providers.downloads.documents/document/raw:/storage/emulated/0/Download/Fonts.txt",
                "Text file test"
            )
        )

        /* Adapter */
        adapter = FileAdapter(files)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        /* */
        adapter.onClick = ::onRowClick

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition

                val file = files[pos]
            }
        })
        itemTouchHelper.attachToRecyclerView(view.findViewById(R.id.file_recyclerview))
    }

    override fun onStart() {
        super.onStart()

        val floating_button = requireActivity().findViewById<FloatingActionButton>(R.id.fab)
        floating_button.setOnClickListener { openFileDialog() }
    }

    override fun onResume() {
        super.onResume()

        val floating_button = requireActivity().findViewById<FloatingActionButton>(R.id.fab)
        floating_button.setOnClickListener { openFileDialog() }
    }

    private fun getUIBindings(view: View) {
        recyclerView = view.findViewById(R.id.file_recyclerview)
    }

    private fun onRowClick(file: FileItem, pos: Int) {
        try {
            val uri = Uri.parse(file.uri)
            val mimeType = requireContext().contentResolver.getType(uri) ?: getMimeTypeFromExtension(file.uri)
            Log.d("FILEMANAGER", "Opening: ${uri}, ${mimeType}")

            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setDataAndType(uri, mimeType)

            // Check if there's an app that can handle the file
            if (intent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(Intent.createChooser(intent, "Open file with"))
            } else {
                Toast.makeText(requireContext(), "No app found to open this file", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Failed to open file: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getMimeTypeFromExtension(filePath: String): String? {
        val extension = filePath.substringAfterLast('.', "").lowercase()
        return when (extension) {
            "pdf" -> "application/pdf"
            "doc", "docx" -> "application/msword"
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            else -> null // Add more types as needed
        }
    }

    private fun onLongClick(file: FileItem, pos: Int) {
        val editText = EditText(requireContext())
        editText.setText(file.displayName)

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Rename display name")
        builder.setView(editText)

        builder.setPositiveButton("Save") { _, _ ->
            file.displayName = editText.text.toString()
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    private fun openFileDialog() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)

        startActivityForResult(intent, CONF.FILE_CHOOSING_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CONF.FILE_CHOOSING_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            uri?.let {
                Log.d("FILEMANAGER", "File add: original_uri = ${it}")

                val destinationDir = File(requireContext().filesDir, "CopiedFiles")
                val temp = copyFileFromUri(requireContext(), it, destinationDir)
                temp?.let {
                    Log.d("FILEMANAGER", "Copied file: uri = ${it}")
                    files.add(FileItem(it.toString(), "File"))
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    fun copyFileFromUri(
        context: Context, uri: Uri, destinationDir: File, destinationFileName: String? = null
    ): File? {
        try {
            // Ensure the destination directory exists
            if (!destinationDir.exists()) {
                destinationDir.mkdirs()
            }

            // Determine the target file name
            val fileName = destinationFileName ?: uri.lastPathSegment ?: "default_file"
            var destinationFile = File(destinationDir, fileName)

            // Handle duplicate file names by appending a count
            var count = 1
            while (destinationFile.exists()) {
                val nameWithoutExtension = destinationFile.nameWithoutExtension
                val extension = destinationFile.extension
                destinationFile = File(destinationDir, "$nameWithoutExtension($count).$extension")
                count++
            }

            // Open input stream from the URI
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            if (inputStream == null) {
                Log.e("FILEMANAGER", "Failed to open input stream for URI: $uri")
                return null
            }

            // Create an output stream for the destination file
            val outputStream: OutputStream = FileOutputStream(destinationFile)
            val buffer = ByteArray(1024)
            var length: Int
            while (inputStream.read(buffer).also { length = it } > 0) {
                outputStream.write(buffer, 0, length)
            }

            // Close streams
            inputStream.close()
            outputStream.close()

            return destinationFile
        } catch (e: Exception) {
            Log.e("FILEMANAGER", "Error copying file: ${e.message}")
            e.printStackTrace()
            return null
        }
    }
}