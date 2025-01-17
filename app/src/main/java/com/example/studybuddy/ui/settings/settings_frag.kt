package com.example.studybuddy.ui.settings

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.studybuddy.R

class settings_frag : Fragment() {

    companion object {
        fun newInstance() = settings_frag()
    }

    private val viewModel: settings_vm by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }
}