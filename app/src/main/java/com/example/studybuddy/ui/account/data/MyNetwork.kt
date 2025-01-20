package com.example.studybuddy.ui.account.data

import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

// Define the API Service Interface
interface ApiService {
    @POST
    fun sendPostRequest(@Url url: String, @Body body: RequestBody): Call<ResponseBody>
}

data class ResponseFormat(val message: String, val success: Boolean)
data class RegisterRequestFormat(val username: String, val email: String, val password: String)
data class LoginRequestFormat(val username: String, val password: String)
data class LoginRequestFormat_Google(val idToken: String)

class MyNetwork {
    fun <T> makeRequestBody(payload: T): RequestBody {
        val json = Gson().toJson(payload)
        val requestBody = RequestBody.create("application/json".toMediaType(), json)

        return requestBody
    }

    // http://example.com/register
    // baseUrl: http://example.com/ (with the slash at the end)
    // endPoint: register
    fun <T> sendPostRequest(baseUrl: String, endPoint: String, payload: T, callback: (Pair<String, Boolean>) -> Unit) {
        /* Variables */
        val retrofit = Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())  // Add Gson converter for JSON parsing
            .build()
        val apiService = retrofit.create(ApiService::class.java)

        val requestBody = makeRequestBody(payload)

        /* API call */
        val call = apiService.sendPostRequest(endPoint, requestBody)


        call.enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                val result = onReponseHandle(response)
                callback(result)
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callback(Pair("Error: ${t.message}", false))
            }
        })
    }
}

fun onReponseHandle(response: retrofit2.Response<ResponseBody>): Pair<String, Boolean> {
    if (!response.isSuccessful) {
        var body = response.errorBody()
        body?.let {
            val jsonString = it.string()

            val apiResponse = Gson().fromJson(jsonString, ResponseFormat::class.java)

            return Pair(apiResponse.message, apiResponse.success)
        }
    }

    var body = response.body()
    body?.let {
        val jsonString = it.string()

        val apiResponse = Gson().fromJson(jsonString, ResponseFormat::class.java)

        return Pair(apiResponse.message, apiResponse.success)
    }
    return Pair("Error, code: ${response.code()}", false)
}