package com.example.studybuddy.ui.course

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studybuddy.CourseViewModelFactory
import com.example.studybuddy.R
import com.example.studybuddy.data.repository.CourseRepository
import com.example.studybuddy.data.local.DatabaseProvider

import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CourseFragment : Fragment() {

    // Initialize Realm and ViewModel
    private lateinit var viewModel: CourseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Khởi tạo repository và factory
        val repository = CourseRepository(DatabaseProvider.getDatabase())
        val factory = CourseViewModelFactory(repository)

        // Sử dụng factory để tạo ViewModel
        viewModel = ViewModelProvider(this, factory)[CourseViewModel::class.java]


        val rootView = inflater.inflate(R.layout.fragment_course, container, false)

        // Set up RecyclerView
        setupRecyclerView(rootView)

        // Add button for adding new courses
        setupAddButton(rootView)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize default courses if needed
        viewModel.initializeDefaultCourses()
    }

    private fun setupRecyclerView(rootView: View) {
        val recyclerView: RecyclerView = rootView.findViewById(R.id.recyclerViewCourses)
        val emptyTextView: TextView = rootView.findViewById(R.id.textViewEmptyCourses)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = CourseAdapter(
            onAction = { course, action ->
                when (action) {
                    CourseAdapter.Action.EDIT -> navigateToEditCourse(course.id)
                    CourseAdapter.Action.DELETE -> viewModel.deleteCourse(course)
                }
            },
            onItemClick = { course ->
                navigateToCourseDetails(course.id)
            }
        )

        recyclerView.adapter = adapter

        // Collect data from Flow
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.courses.collectLatest { courses ->
                adapter.submitList(courses)
                emptyTextView.visibility = if (courses.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }


    private fun setupAddButton(rootView: View) {
        val addButton: Button = rootView.findViewById(R.id.buttonNewCourse)
        addButton.setOnClickListener {
            navigateToAddCourse()
        }
    }

    private fun navigateToAddCourse() {
        val action = CourseFragmentDirections.actionCourseFragToManageCourseFrag(
            courseId = -1, // Giá trị mặc định cho chế độ ADD
            mode = "ADD"
        )
        findNavController().navigate(action)
    }

    private fun navigateToEditCourse(courseId: Int) {
        // Navigate to EditCourseFragment
        val action = CourseFragmentDirections.actionCourseFragToManageCourseFrag(
            courseId = courseId,
            mode = "EDIT"
        )
        findNavController().navigate(action)
    }

    private fun navigateToCourseDetails(courseId: Int) {
        val action = CourseFragmentDirections.actionCourseFragToManageCourseFrag(
            courseId = courseId,
            mode = "DETAILS"
        )
        findNavController().navigate(action)
    }
}
