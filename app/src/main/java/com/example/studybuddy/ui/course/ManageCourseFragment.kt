package com.example.studybuddy.ui.course

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.studybuddy.R

class ManageCourseFragment : Fragment() {

    companion object {
        fun newInstance() = ManageCourseFragment()
    }

    private val viewModel: ManageCourseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_manage_course, container, false)
    }

    private val args: ManageCourseFragmentArgs by navArgs() // Nhận tham số qua SafeArgs

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val courseId = args.courseId
        val mode = args.mode

        when (mode) {
            "ADD" -> setupAddMode()
            "EDIT" -> setupEditMode(courseId)
            "DETAILS" -> setupDetailsMode(courseId)
        }
    }

    private fun setupAddMode() {
        // Logic cho chế độ ADD
    }

    private fun setupEditMode(courseId: Long) {
        // Logic cho chế độ EDIT
    }

    private fun setupDetailsMode(courseId: Long) {
        // Logic cho chế độ DETAILS
    }

}