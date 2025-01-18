package com.example.studybuddy.ui.course

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.studybuddy.AppViewModelFactory
import com.example.studybuddy.R
import com.example.studybuddy.data.local.DatabaseProvider
import com.example.studybuddy.data.local.model.CourseModel
import com.example.studybuddy.data.repository.CourseRepository
import com.example.studybuddy.databinding.FragmentManageCourseBinding
import com.example.studybuddy.utilities.formatDate
import com.example.studybuddy.utilities.formatTime
import com.example.studybuddy.utilities.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ManageCourseFragment : Fragment() {

    companion object {
        fun newInstance() = ManageCourseFragment()
    }

    private lateinit var viewModel: ManageCourseViewModel
    private val args: ManageCourseFragmentArgs by navArgs()
    private var _binding: FragmentManageCourseBinding? = null
    private val binding get() = _binding!!
    private var mode: String? = null
    private var courseId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManageCourseBinding.inflate(inflater, container, false)
        // Khởi tạo repository và factory
        val repository = CourseRepository(DatabaseProvider.getDatabase())
        val factory = AppViewModelFactory(repository)

        // Sử dụng factory để tạo ViewModel
        viewModel = ViewModelProvider(this, factory)[ManageCourseViewModel::class.java]

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
        courseId = args.courseId

        // Cấu hình Spinner
        setupDayOfWeekSpinner()
        if (mode != "DETAILS") {
            // Hiển thị DatePickerDialog khi nhấn vào editTextStartDate
            binding.editTextStartDate.setOnClickListener {
                showDatePicker { selectedDate ->
                    binding.editTextStartDate.setText(selectedDate)
                }
            }

            // Hiển thị DatePickerDialog khi nhấn vào editTextEndDate
            binding.editTextEndDate.setOnClickListener {
                showDatePicker { selectedDate ->
                    binding.editTextEndDate.setText(selectedDate)
                }
            }

            // Hiển thị TimePickerDialog khi nhấn vào editTextStartTime
            binding.editTextStartTime.setOnClickListener {
                showTimePicker { selectedTime ->
                    binding.editTextStartTime.setText(selectedTime)
                }
            }

            // Hiển thị TimePickerDialog khi nhấn vào editTextEndTime
            binding.editTextEndTime.setOnClickListener {
                showTimePicker { selectedTime ->
                    binding.editTextEndTime.setText(selectedTime)
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

        binding.editTextStartTime.setText(formatTime(now))
        binding.editTextEndTime.setText(formatTime(now))
        binding.editTextStartDate.setText(formatDate(defaultStartDate))
        binding.editTextEndDate.setText(formatDate(defaultEndDate))

        binding.buttonSaveCourse.setOnClickListener {
            val newCourse = createCourseFromInput()
            viewModel.addCourse(newCourse, requireContext())
        }
    }

    private fun setupEditMode() {
        viewModel.getCourseById(courseId!!).observe(viewLifecycleOwner) { course ->
            course?.let {
                populateFields(it)
                setEditable(true)
            }
        }

        binding.buttonSaveCourse.setOnClickListener {
            val updatedCourse = createCourseFromInput()
            viewModel.updateCourse(updatedCourse, requireContext()) { isSuccess ->
                if (isSuccess) {
                    findNavController().popBackStack()
                }
            }
        }

    }

    private fun setupDetailsMode() {
        viewModel.getCourseById(courseId!!).observe(viewLifecycleOwner) { course ->
            course?.let {
                populateFields(it)
                setEditable(false)
            }
        }
    }

    private fun setupDayOfWeekSpinner() {
        val daysOfWeek = resources.getStringArray(R.array.days_of_week)

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            daysOfWeek
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerDayOfWeek.adapter = adapter
    }

    private fun setEditable(editable: Boolean) {
        val inputType = if (editable) InputType.TYPE_CLASS_TEXT else InputType.TYPE_NULL
        binding.editTextCourseName.inputType = inputType

        if (editable) {
            binding.spinnerFrame.visibility = View.VISIBLE
            binding.textViewDayOfWeek.visibility = View.GONE
        } else {
            binding.spinnerFrame.visibility = View.GONE
            binding.textViewDayOfWeek.visibility = View.VISIBLE
            binding.textViewDayOfWeek.setText(binding.spinnerDayOfWeek.selectedItem.toString())
        }


        binding.editTextStartTime.inputType = inputType
        binding.editTextEndTime.inputType = inputType
        binding.editTextStartDate.inputType = inputType
        binding.editTextEndDate.inputType = inputType
        binding.editTextRoom.inputType = inputType
        binding.checkBoxReminder.inputType = inputType
        binding.buttonSaveCourse.visibility = if (editable) View.VISIBLE else View.GONE
    }

    private fun populateFields(course: CourseModel) {
        Log.d("populateFields startTime", course.startTime.toString())
        binding.editTextCourseName.setText(course.name)

        // Đặt giá trị ngày trong tuần vào Spinner
        binding.spinnerDayOfWeek.setSelection(course.dayOfWeek - 1) // Enum bắt đầu từ 1

        // Sử dụng hàm formatTime và formatDate từ CourseModel
        binding.editTextStartTime.setText(formatTime(course.startTime))
        binding.editTextEndTime.setText(formatTime(course.endTime))
        binding.editTextStartDate.setText(formatDate(course.startDate))
        binding.editTextEndDate.setText(formatDate(course.endDate))

        binding.checkBoxReminder.isChecked = course.hasReminder
        binding.editTextRoom.setText(course.room ?: "")
    }

    private fun createCourseFromInput(): CourseModel {
        return if (courseId != null) {
            // Tạo một đối tượng tạm thời và thực hiện cập nhật
            CourseModel().apply {
                id = courseId!!
                name = binding.editTextCourseName.text.toString()
                dayOfWeek = binding.spinnerDayOfWeek.selectedItemPosition + 1 // Enum bắt đầu từ 1
                startTime = stringToTimestamp(binding.editTextStartTime.text.toString(), "HH:mm")
                endTime = stringToTimestamp(binding.editTextEndTime.text.toString(), "HH:mm")
                startDate = stringToTimestamp(binding.editTextStartDate.text.toString(), "dd/MM/yyyy")
                endDate = stringToTimestamp(binding.editTextEndDate.text.toString(), "dd/MM/yyyy")
                hasReminder = binding.checkBoxReminder.isChecked
                room = binding.editTextRoom.text.toString()

                // Cập nhật trong ViewModel
                viewModel.updateCourse(this, requireContext()) { isSuccess ->
                    if (isSuccess) {
                        findNavController().popBackStack()
                    }
                }
            }
        } else {
            // Tạo đối tượng mới
            CourseModel().apply {
                name = binding.editTextCourseName.text.toString()
                dayOfWeek = binding.spinnerDayOfWeek.selectedItemPosition + 1 // Enum bắt đầu từ 1
                startTime = stringToTimestamp(binding.editTextStartTime.text.toString(), "HH:mm")
                endTime = stringToTimestamp(binding.editTextEndTime.text.toString(), "HH:mm")
                startDate = stringToTimestamp(binding.editTextStartDate.text.toString(), "dd/MM/yyyy")
                endDate = stringToTimestamp(binding.editTextEndDate.text.toString(), "dd/MM/yyyy")
                hasReminder = binding.checkBoxReminder.isChecked
                room = binding.editTextRoom.text.toString()

                // Lưu mới trong ViewModel
                viewModel.addCourse(this, requireContext())
            }
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
        return date.time // Trả về timestamp dạng milliseconds
    }

}