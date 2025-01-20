package com.example.studybuddy.ui.account

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.studybuddy.R
import com.example.studybuddy.ui.account.data.LoginRequestFormat
import com.example.studybuddy.ui.account.data.LoginRequestFormat_Google
import com.example.studybuddy.ui.account.data.MyNetwork
import com.example.studybuddy.utilities.CONF
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginFragment : Fragment() {
    lateinit var username_edittext: EditText
    lateinit var email_edittext: EditText
    lateinit var password_edittext: EditText
    lateinit var send_button: Button
    lateinit var google_imagebutton: ImageButton

    private lateinit var googleSignInClient: GoogleSignInClient
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configure Google Sign-In
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("502671157835-1bovdk3cu8qai5q288cejogiajmii8ge.apps.googleusercontent.com") // Replace with your client ID
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions)
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

        send_button.setOnClickListener {
            val client = MyNetwork()
            val email_baseUrl = "https://buddy.keyous.duckdns.org/"
            val email_endPoint = "login"
            val email_payload = LoginRequestFormat(
                username_edittext.text.toString(),
                password_edittext.text.toString()
            )

            onEmailLogin(client, email_baseUrl, email_endPoint, email_payload)
        }

        google_imagebutton.setOnClickListener {
            onGoogleButtonClicked()
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

    fun <T> onEmailLogin(client: MyNetwork, baseUrl: String, endPoint: String, payload: T) {
        lifecycleScope.launch {
            getRegisterResponse(client, baseUrl, endPoint, payload) { response ->
                Log.d("MYNETWORK", "Message: ${response.first}, success: ${response.second}")
                response

                Toast.makeText(requireContext(), response.first, Toast.LENGTH_SHORT).show()

                if (response.second) {
                    Log.d("MYNETWORK", "Sucessfully logged in EMAIL")
                    val action = LoginFragmentDirections.actionLoginFragToHomeFrag()
                    findNavController().navigate(action)
//                    val navController = findNavController()
//                    navController.navigate(R.id.action_login_frag_to_home_frag)
                }
            }
        }
    }

    fun <T> onGoogleLogin(client: MyNetwork, baseUrl: String, endPoint: String, payload: T) {
        lifecycleScope.launch {
            getRegisterResponse(client, baseUrl, endPoint, payload) { response ->
                Log.d("MYNETWORK", "Message: ${response.first}, success: ${response.second}")
                response

                Toast.makeText(requireContext(), response.first, Toast.LENGTH_SHORT).show()

                if (response.second) {
                    Log.d("MYNETWORK", "Sucessfully logged in GOOGLE")
                    val action = LoginFragmentDirections.actionLoginFragToHomeFrag()
                    findNavController().navigate(action)
//                    val navController = findNavController()
//                    navController.navigate(R.id.action_login_frag_to_home_frag)
                }
            }
        }
    }

    private fun onGoogleButtonClicked() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, CONF.GOOGLE_SIGN_IN_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching GoogleSignInClient.getSignInIntent()
        if (requestCode == CONF.GOOGLE_SIGN_IN_REQUEST_CODE) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, retrieve the ID token
            val idToken = account?.idToken
            val email = account?.email

            Log.d("MYNETWORK", "ID Token: $idToken")
            Log.d("MYNETWORK", "Email: $email")

            // Send the ID token to your server for verification
            sendToServer(idToken!!)

        } catch (e: ApiException) {
            Log.d("MYNETWORK", "signInResult:failed code=${e.statusCode}")
        }
    }

    // for google sign in
    fun sendToServer(idToken: String) {
        val client = MyNetwork()
        val baseUrl_Google = "https://buddy.keyous.duckdns.org/"
        val endPoint_Google = "login/google"
        val payload_Google = LoginRequestFormat_Google(idToken)

        onGoogleLogin(client, baseUrl_Google, endPoint_Google, payload_Google)
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
        google_imagebutton = view.findViewById(R.id.google_imagebutton)
        send_button = view.findViewById(R.id.account_button)
    }
}