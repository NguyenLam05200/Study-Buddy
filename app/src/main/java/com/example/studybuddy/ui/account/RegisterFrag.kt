package com.example.studybuddy.ui.account

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.studybuddy.R
import com.example.studybuddy.ui.account.data.MyNetwork
import com.example.studybuddy.ui.account.data.RegisterRequestFormat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class RegisterFrag : Fragment() {
    lateinit var username_edittext: EditText
    lateinit var email_edittext: EditText
    lateinit var password_edittext: EditText
    lateinit var send_button: Button

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getUIBindings(view)

        val client = MyNetwork()
        val register_baseUrl = "https://buddy.alouve.top/"
        val register_endPoint = "register"

        send_button.setOnClickListener() {
            val register_payload = RegisterRequestFormat(
                username_edittext.text.toString(),
                email_edittext.text.toString(),
                password_edittext.text.toString()
            )
            lifecycleScope.launch {
                getRegisterResponse(client, register_baseUrl, register_endPoint, register_payload) { response ->
                    Log.d("MYNETWORK", "Message: ${response.first}, success: ${response.second}")
                    response

                    Toast.makeText(requireContext(), response.first, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        val floating_button = requireActivity().findViewById<FloatingActionButton>(R.id.fab)
        floating_button.hide()
    }

    override fun onResume() {
        super.onResume()

        val floating_button = requireActivity().findViewById<FloatingActionButton>(R.id.fab)
        floating_button.hide()
    }

    suspend fun <T> getRegisterResponse(
        client: MyNetwork,
        register_baseUrl: String,
        register_endPoint: String,
        register_payload: T,
        callback: (Pair<String, Boolean>) -> Unit
    ) {
        withContext(Dispatchers.IO)
        {
            client.sendPostRequest(register_baseUrl, register_endPoint, register_payload) { response ->
                callback(response)
            }
        }

    }

    private fun getUIBindings(view: View) {
        username_edittext = view.findViewById(R.id.account_username_edittext)
        email_edittext = view.findViewById(R.id.account_email_edittext)
        password_edittext = view.findViewById(R.id.account_password_edittext)
        send_button = view.findViewById(R.id.account_button)
    }
}