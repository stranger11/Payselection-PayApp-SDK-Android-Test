package payselection.payments.sdk.api.utils

import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import payselection.payments.sdk.api.models.ServerError
import payselection.payments.sdk.exceptions.RequestException
import payselection.payments.sdk.exceptions.UnexpectedResponseException
import payselection.payments.sdk.utils.Result
import retrofit2.Response
import java.net.UnknownHostException

suspend fun <T : Any> safeApiResult(call: suspend () -> Response<T>): Result<T> {
    try {
        val response = call.invoke()
        if (response.isSuccessful) {
            return Result.Success(response.body())
        } else {
            response.errorBody()?.let { body ->
                val error = toServerError(body.string())
                error?.let {
                    return Result.Error(RequestException(it))
                }
            }
        }
        return Result.Error(RequestException(response.code().toString(), response.message()))
    } catch (e: IllegalStateException) {
        return Result.Error(UnexpectedResponseException())
    } catch (e: UnknownHostException) {
        return Result.Error(e)
    } catch (e: Exception) {
        return Result.Error(e)
    }
}

internal fun toServerError(error: String): ServerError? {
    return try {
        GsonBuilder()
            .setLenient()
            .create()
            .fromJson(error, ServerError::class.java)
    } catch (e: JsonSyntaxException) {
        null
    }
}
