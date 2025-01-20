package com.example.studybuddy.ui.deadline

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.studybuddy.DeadlineViewModelFactory
import com.example.studybuddy.R
import com.example.studybuddy.data.local.DatabaseProvider
import com.example.studybuddy.data.local.model.DeadlineModel
import com.example.studybuddy.data.repository.DeadlineRepository
import com.example.studybuddy.databinding.FragmentManageDeadlineBinding
import com.example.studybuddy.utilities.formatDate
import com.example.studybuddy.utilities.formatTime
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ManageDeadlineFragment : Fragment() {

    companion object {
        fun newInstance() = ManageDeadlineFragment()
    }

    private lateinit var viewModel: ManageDeadlineViewModel
    private val args: ManageDeadlineFragmentArgs by navArgs()
    private var _binding: FragmentManageDeadlineBinding? = null
    private val binding get() = _binding!!
    private var mode: String? = null
    private var deadlineId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManageDeadlineBinding.inflate(inflater, container, false)
        // Khởi tạo repository và factory
        val repository = DeadlineRepository(DatabaseProvider.getDatabase())
        val factory = DeadlineViewModelFactory(repository)

        // Sử dụng factory để tạo ViewModel
        viewModel = ViewModelProvider(this, factory)[ManageDeadlineViewModel::class.java]

        viewModel.updateStatus.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                findNavController().popBackStack()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mode = args.mode
        deadlineId = args.deadlineId

        // Cấu hình Spinner với danh sách khóa học
        setupDeadlineSpinner()

        if (mode != "DETAILS") {
            // Hiển thị DatePickerDialog khi nhấn vào editTextStartDate
            binding.editTextDueDate.setOnClickListener {
                showDatePicker { selectedDate ->
                    binding.editTextDueDate.setText(selectedDate)
                }
            }

            // Hiển thị DatePickerDialog khi nhấn vào editTextEndDate
            binding.editTextTime.setOnClickListener {
                showTimePicker { selectedDate ->
                    binding.editTextTime.setText(selectedDate)
                }
            }
        }

        when (mode) {
            "ADD" -> setupAddMode()
            "EDIT" -> setupEditMode()
            "DETAILS" -> setupDetailsMode()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupAddMode() {
        setEditable(true)

        val now = System.currentTimeMillis()
        val defaultStartDate = now
        val defaultEndDate = now + 7 * 24 * 60 * 60 * 1000 // 1 tuần sau

        binding.editTextTime.setText(formatTime(now))
        binding.editTextDueDate.setText(formatDate(defaultStartDate))

        binding.buttonSaveDeadline.setOnClickListener {
            val newDeadline = createDeadlineFromInput()
            viewModel.addDeadline(newDeadline, requireContext())
        }
    }

    private fun setupEditMode() {
        viewModel.getDeadlineById(deadlineId!!).observe(viewLifecycleOwner) { deadline ->
            deadline?.let {
                populateFields(it)
                setEditable(true)
            }
        }

        binding.buttonSaveDeadline.setOnClickListener {
            val updatedDeadline = createDeadlineFromInput()
            viewModel.updateDeadline(updatedDeadline, requireContext()) { isSuccess ->
                if (isSuccess) {
                    findNavController().popBackStack()
                }
            }
        }

    }

    private fun setupDetailsMode() {
        viewModel.getDeadlineById(deadlineId!!).observe(viewLifecycleOwner) { deadline ->
            deadline?.let {
                populateFields(it)
                setEditable(false)
            }
        }
    }

    private fun setEditable(editable: Boolean) {
        // Apply custom enable/disable logic for EditText
        toggleEditText(binding.editTextDeadlineName, editable, true)
        toggleEditText(binding.editTextTime, editable)
        toggleEditText(binding.editTextDueDate, editable)

        // Handle Spinner visibility
        if (editable) {
            binding.spinnerFrame.visibility = View.VISIBLE
            binding.textViewDayOfWeek.visibility = View.GONE
        } else {
            binding.spinnerFrame.visibility = View.GONE
            binding.textViewDayOfWeek.visibility = View.VISIBLE
            toggleEditText(binding.textViewDayOfWeek, editable)
            binding.textViewDayOfWeek.setText(binding.spinnerCourse.selectedItem.toString())
        }

        // Toggle button visibility
        binding.buttonSaveDeadline.visibility = if (editable) View.VISIBLE else View.GONE
    }

    private fun toggleEditText(editText: View, enabled: Boolean, isSetFocus: Boolean = false) {
        if (isSetFocus) {
            editText.isFocusableInTouchMode = enabled
            editText.isFocusable = enabled
        }
        editText.isEnabled = enabled

        if (!enabled) {
            editText.setBackgroundResource(android.R.color.transparent)
            (editText as? android.widget.TextView)?.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.primaryTextColor)
            )
        }
    }
    private fun populateFields(deadline: DeadlineModel) {
        Log.d("populateFields startTime", deadline.time.toString())
        binding.editTextDeadlineName.setText(deadline.name)

        // Tìm index của courseId trong danh sách Course
        val courseIndex = getCourseIndexById(deadline.courseId)
        if (courseIndex != -1) {
            binding.spinnerCourse.setSelection(courseIndex) // Đặt Spinner về đúng vị trí
        } else {
            Log.e("populateFields", "Course not found for id: ${deadline.courseId}")
        }

        // Sử dụng hàm formatTime và formatDate từ DeadlineModel
        binding.editTextTime.setText(formatTime(deadline.time))
        binding.editTextDueDate.setText(formatDate(deadline.dueDate))
        binding.checkBoxReminder.isChecked = deadline.hasReminder
    }

    private fun getCourseIndexById(courseId: Int): Int {
        val courses = viewModel.getAllCourses() // Lấy danh sách khóa học từ ViewModel
        return courses.indexOfFirst { it.id == courseId } // Tìm index của courseId
    }
    private fun createDeadlineFromInput(): DeadlineModel {
        val selectedCourseIndex = binding.spinnerCourse.selectedItemPosition // Lấy vị trí được chọn
        val selectedCourse = viewModel.getAllCourses()[selectedCourseIndex] // Lấy CourseModel từ danh sách

        return DeadlineModel().apply {
            id = deadlineId ?: 0 // Nếu là thêm mới, đặt id = 0
            name = binding.editTextDeadlineName.text.toString()
            courseId = selectedCourse.id // Lấy courseId từ CourseModel
            courseName = selectedCourse.name // Lấy courseName từ CourseModel
            time = stringToTimestamp(binding.editTextTime.text.toString(), "HH:mm")
            dueDate = stringToTimestamp(binding.editTextDueDate.text.toString(), "dd/MM/yyyy")
            hasReminder = binding.checkBoxReminder.isChecked
        }
    }

    private fun showDatePicker(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                // Định dạng ngày khi chọn từ dialog
                val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                calendar.set(selectedYear, selectedMonth, selectedDay)
                val formattedDate =
                    formatter.format(calendar.time) // Format ngày theo định dạng dd-MM-yyyy
                onDateSelected(formattedDate) // Gọi callback với ngày đã được định dạng
            }, year, month, day)

        datePickerDialog.show()
    }

    private fun showTimePicker(onTimeSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog =
            TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
                // Định dạng giờ khi chọn từ dialog
                val formattedTime =
                    String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute)
                onTimeSelected(formattedTime) // Gọi callback với giờ đã được định dạng
            }, hour, minute, true) // true để dùng định dạng 24h

        timePickerDialog.show()
    }

    private fun stringToTimestamp(dateTime: String, pattern: String = "dd/MM/yyyy HH:mm"): Long {
        val formatter = SimpleDateFormat(pattern, Locale.getDefault())
        val date = formatter.parse(dateTime) ?: throw IllegalArgumentException("Invalid date format")

        Log.d("_____Test", "dateTime: $dateTime")
        Log.d("_____Test", "stringToTimestamp: ${date.time}")
        return date.time // Trả về timestamp dạng milliseconds
    }

    private fun setupDeadlineSpinner() {
        val deadlines = viewModel.getAllCourses() // Truy vấn danh sách Deadline từ ViewModel
        val deadlineNames = deadlines.map { it.name } // Lấy tên các khóa học

        // Tạo ArrayAdapter và gắn vào Spinner
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            deadlineNames
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCourse.adapter = adapter
    }

}