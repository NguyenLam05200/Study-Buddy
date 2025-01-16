package com.example.studybuddy.ui.settings

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.example.studybuddy.R
import com.example.studybuddy.data.*

class settings_frag : Fragment() {
    lateinit var language_spinner: Spinner
    lateinit var dateformat: Spinner
    lateinit var fontsize: Spinner

    companion object {
        fun newInstance() = settings_frag()
    }

    private val viewModel: settings_vm by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* Get elements from XML */
        getElementBindings(view)

        /* Adapters */
        val language_adapter = ArrayAdapter<String>(
            requireContext(), android.R.layout.simple_spinner_item, CONF.languages
        )
        language_adapter.setDropDownViewResource(R.layout.element_settings_spinner_dropdown)

        val fontsize_adapter = ArrayAdapter<String>(
            requireContext(), android.R.layout.simple_spinner_item, CONF.fontsizes
        )
        fontsize_adapter.setDropDownViewResource(R.layout.element_settings_spinner_dropdown)

        val datetime_adapter = ArrayAdapter<String>(
            requireContext(), android.R.layout.simple_spinner_item, CONF.datetime_formats
        )
        datetime_adapter.setDropDownViewResource(R.layout.element_settings_spinner_dropdown)

        /* Bind adapters to spinners */
        language_spinner.adapter = language_adapter
        dateformat.adapter = fontsize_adapter
        fontsize.adapter = datetime_adapter

        /* */
        val about_textview = view.findViewById<TextView>(R.id.settings_about_textview)
        about_textview.setOnClickListener() {
            showInfoDialog(
                requireContext(), "About", message = "18120433 - Nguyen Van Lam\n" +
                        "20120339 - Nguyen Nhat Nguyen"
            )
        }
    }

    fun getElementBindings(view: View) {
        language_spinner = view.findViewById<Spinner>(R.id.settings_language_spinner)
        dateformat = view.findViewById<Spinner>(R.id.settings_dateformat_spinner)
        fontsize = view.findViewById<Spinner>(R.id.settings_fontsize_spinner)
    }
}
