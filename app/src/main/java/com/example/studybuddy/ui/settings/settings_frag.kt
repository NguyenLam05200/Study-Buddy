package com.example.studybuddy.ui.settings

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.example.studybuddy.R
import com.example.studybuddy.utilities.*

class settings_frag : Fragment() {
    var isUserInteraction = false
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* Get elements from XML */
        getUIBindings(view)

        /* Spinner adapters */
        setSpinnerAdapters()

        /* Prevent language spinner from being selected on fragment loaded */
        language_spinner.setOnTouchListener { _, _ ->
            isUserInteraction = true
            false // Allow the spinner to handle the touch event
        }

        /* Language change */
        language_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isUserInteraction) {
                    val selected_language = CONF.languages[position]
                    Toast.makeText(requireContext(), "Selected: ${selected_language}", Toast.LENGTH_SHORT).show()

                    val language_code = CONF.language_codes[position]
                    updateAppUI(language_code)

                    saveLanguage(selected_language)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        /* Update spinner to current language */
        val savedlanguage = getSavedLanguage()
        savedlanguage?.let {
            val pos = CONF.languages.indexOf(savedlanguage)
            language_spinner.setSelection(pos)
        }

        /* */
        val about_textview = view.findViewById<TextView>(R.id.settings_about_textview)
        about_textview.setOnClickListener() {
            showInfoDialog(
                requireContext(),
                "About",
                message = "18120433 - Nguyen Van Lam\n" + "20120339 - Nguyen Nhat Nguyen"
            )
        }
    }

    private fun updateAppUI(languageCode: String) {
        val locale = java.util.Locale(languageCode)
        java.util.Locale.setDefault(locale)

        val config = android.content.res.Configuration(resources.configuration)
        config.setLocale(locale)

        // Update the configuration
        requireContext().resources.updateConfiguration(
            config, requireContext().resources.displayMetrics
        )

        // Optionally reload the fragment/activity to reflect language change
        requireActivity().recreate()
    }

    private fun saveLanguage(language: String) {
        PreferencesManager.putString(CONF.LANGUAGE_KEY, language)
    }

    private fun getSavedLanguage(): String? {
        return PreferencesManager.getString(CONF.LANGUAGE_KEY, null)
    }

    private fun getUIBindings(view: View) {
        language_spinner = view.findViewById<Spinner>(R.id.settings_language_spinner)
        dateformat = view.findViewById<Spinner>(R.id.settings_dateformat_spinner)
        fontsize = view.findViewById<Spinner>(R.id.settings_fontsize_spinner)
    }

    private fun setSpinnerAdapters() {
        /* Creating adapters */
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
    }
}
