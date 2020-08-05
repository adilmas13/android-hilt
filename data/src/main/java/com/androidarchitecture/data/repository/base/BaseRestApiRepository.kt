package com.androidarchitecture.data.repository.base

import com.androidarchitecture.data.entities.ApiResponseWrapperEntity
import com.androidarchitecture.data.retrofit.ApiService
import com.androidarchitecture.data.retrofit.ApiServiceBuilder
import com.androidarchitecture.domain.NetworkMonitor
import com.androidarchitecture.domain.exceptions.ApiException
import com.androidarchitecture.domain.models.Result
import com.google.gson.Gson
import retrofit2.Response

open class BaseRestApiRepository(networkMonitor: NetworkMonitor) {

    open var api: ApiService = ApiServiceBuilder.create(networkMonitor)
//    private val json = Json(JsonConfiguration.Stable)

    internal fun <T, R> parseResult(response: Response<T>, parser: (T) -> R): Result<R> {
        return if (response.isSuccessful && response.body() !== null)
            Result.success(parser.invoke(response.body()!!))
        else
            Result.error(getError(response))
    }

    private fun <T> getError(response: Response<T>): ApiException {
        val errorString = response.errorBody()?.toString() ?: ""
        val errorObject = try {
            Gson().fromJson(errorString, ApiResponseWrapperEntity::class.java)
//            json.parse(ApiResponseWrapper.serializer(response.), errorString)
        } catch (e: Exception) {
            ApiResponseWrapperEntity<Any>(
                status = false,
                error = "Something went wrong"
            )
        }
        return ApiException(errorObject.error)
    }
}
