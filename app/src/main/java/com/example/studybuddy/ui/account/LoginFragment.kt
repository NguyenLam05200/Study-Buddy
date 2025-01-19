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
import androidx.navigation.fragment.findNavController
import com.example.studybuddy.R
import com.example.studybuddy.ui.account.data.LoginRequestFormat
import com.example.studybuddy.ui.account.data.MyNetwork
import com.example.studybuddy.ui.account.data.RegisterRequestFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginFragment : Fragment() {
    lateinit var username_edittext: EditText
    lateinit var email_edittext: EditText
    lateinit var password_edittext: EditText
    lateinit var send_button: Button

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getUIBindings(view)

        val client = MyNetwork()
        val register_baseUrl = "https://buddy.keyous.duckdns.org/"
        val register_endPoint = "login"

        send_button.setOnClickListener() {
            val register_payload = LoginRequestFormat(
                username_edittext.text.toString(),
                password_edittext.text.toString()
            )

            lifecycleScope.launch {
                getRegisterResponse(client, register_baseUrl, register_endPoint, register_payload) { response ->
                    Log.d("MYNETWORK", "Message: ${response.first}, success: ${response.second}")
                    response

                    Toast.makeText(requireContext(), response.first, Toast.LENGTH_SHORT).show()

                    if (response.second) {
                        Log.d("MYNETWORK", "Sucessfully logged in")
                        val navController = findNavController()
//                        navController.navigate(R.id.somewhere)
                    }
                }
            }
        }
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
        password_edittext = view.findViewById(R.id.account_password_edittext)
        send_button = view.findViewById(R.id.account_button)
    }
}