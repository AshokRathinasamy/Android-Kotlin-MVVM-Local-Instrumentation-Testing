package com.mvvm.localtest.data.source.remote

import retrofit2.Response
import java.io.IOException
import com.mvvm.localtest.data.Result
import com.mvvm.localtest.data.Result.Success
import com.mvvm.localtest.data.Result.Error

abstract class SafeApiRequest {

    suspend fun<T: Any> apiRequest(call: suspend () -> Response<T>): Result<T> {
        val response = call.invoke()
        if (response.isSuccessful){
            return Success(response.body()!!)
        } else {
            return Error(response.code().toString(), response.message().toString())
        }
    }
}

class ApiException(message: String) : IOException(message)