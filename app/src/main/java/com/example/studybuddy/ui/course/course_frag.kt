package com.example.studybuddy.ui.course

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.studybuddy.R

class course_frag : Fragment() {

    companion object {
        fun newInstance() = course_frag()
    }

    private val viewModel: course_vm by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()

        val testbutton : Button = view.findViewById(R.id.testbutton)
        testbutton.setOnClickListener {
            navController.navigate(R.id.course_addcource_frag)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_course, container, false)
    }
}