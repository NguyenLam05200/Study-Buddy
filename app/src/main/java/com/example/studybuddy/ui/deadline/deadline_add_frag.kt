package com.example.studybuddy.ui.deadline

import android.app.DatePickerDialog
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import com.example.studybuddy.R
import java.time.LocalDate

class deadline_add_frag : Fragment() {

    companion object {
        fun newInstance() = deadline_add_frag()
    }

    private val viewModel: deadline_add_vm by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_deadline_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val datebutton = view.findViewById<ImageButton>(R.id.date_button)
        datebutton.setOnClickListener() {
            onDateButtonClick()
        }
    }

    fun onDatePickerResult(selectedYear: Int, selectedMonth: Int, selectedDay: Int) {
        val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"

//        For debugging
        Toast.makeText(requireContext(), "Selected Date: $selectedDate", Toast.LENGTH_SHORT).show()
    }

    fun onDateButtonClick() {
        /*
            If not empty: try to jump to that date when click the button
            If empty: jump to current date
         */
        var parsed_date: LocalDate? = null
        var current_day = LocalDate.now()

        // Create a DatePickerDialog
        val datedialog = DatePickerDialog(requireContext())
        datedialog.setOnDateSetListener(){ _, selectedYear, selectedMonth, selectedDay ->
            onDatePickerResult(selectedYear, selectedMonth, selectedDay)
        }

        if (parsed_date != null)
        {
            // monthValue starts from 1, but datedialog month starts from 0
            datedialog.updateDate(parsed_date.year, parsed_date.monthValue - 1, parsed_date.dayOfMonth)
        }
        else
        {
            // monthValue starts from 1, but datedialog month starts from 0
            datedialog.updateDate(
                current_day.year,
                current_day.monthValue - 1,
                current_day.dayOfMonth)
        }

        datedialog.show()
    }
}