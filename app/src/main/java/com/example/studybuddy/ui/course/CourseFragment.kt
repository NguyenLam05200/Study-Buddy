package com.example.studybuddy.ui.course

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studybuddy.R
import com.example.studybuddy.data.repository.CourseRepository
import com.example.studybuddy.utils.DatabaseProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CourseFragment : Fragment() {
    companion object {
        fun newInstance() = CourseFragment()
    }

    //    private val viewModel: CourseViewModel by viewModels()
    private val viewModel: CourseViewModel by viewModels {
        val dao = DatabaseProvider.getDatabase(requireContext()).courseDao()
        val repository = CourseRepository(dao)
        CourseViewModelFactory(repository)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(R.layout.fragment_course, container, false)

        // Khởi tạo RecyclerView
        setupRecyclerView(rootView)

        // Thêm logic khởi tạo khóa học mặc định
        viewModel.initializeDefaultCourses()

        // Khởi tạo nút thêm khóa học
        setupAddButton(rootView)

        return rootView
//        return inflater.inflate(R.layout.fragment_course, container, false)
    }

    private fun setupRecyclerView(rootView: View) {
        val recyclerView: RecyclerView = rootView.findViewById(R.id.recyclerViewCourses)
        val emptyTextView: TextView = rootView.findViewById(R.id.textViewEmptyCourses)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = CourseAdapter { course, action ->
            when (action) {
                CourseAdapter.Action.EDIT -> navigateToEditCourse(course.id)
                CourseAdapter.Action.DELETE -> viewModel.deleteCourse(course)
            }
        }
        recyclerView.adapter = adapter

        // Quan sát dữ liệu
        viewModel.courses.observe(viewLifecycleOwner) { courses ->
            adapter.submitList(courses)
            // Hiển thị thông báo nếu danh sách trống
            emptyTextView.visibility = if (courses.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun setupAddButton(rootView: View) {
        val addButton: Button = rootView.findViewById(R.id.buttonNewCourse)
        addButton.setOnClickListener {
            navigateToAddCourse()
        }
    }

    private fun navigateToAddCourse() {
        val action = CourseFragmentDirections.actionToAddCourseFrag()
        findNavController().navigate(action)
    }


    private fun navigateToEditCourse(courseId: Int) {
        // Điều hướng đến màn hình chỉnh sửa khóa học
//        val action = CourseFragmentDirections.actionCourseFragmentToEditCourseFragment(courseId)
//        requireView().findNavController().navigate(action)
    }

}