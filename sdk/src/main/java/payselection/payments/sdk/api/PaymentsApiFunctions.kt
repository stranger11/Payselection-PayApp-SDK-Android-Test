package payselection.payments.sdk.api

import com.google.gson.JsonObject
import kotlinx.coroutines.Deferred
import payselection.payments.sdk.api.models.TransactionStatusObject
import payselection.payments.sdk.models.results.pay.PaymentResult
import retrofit2.Response
import retrofit2.http.*

internal interface PaymentsApiFunctions {

    @POST("/payments/requests/public")
    fun pay(@Body data: JsonObject): Deferred<Response<PaymentResult>>


    @Headers("X-REQUEST-AUTH: public")
    @GET("/transactions/{transactionId}")
    fun getTransaction(@Path("transactionId") transactionId: String): Deferred<Response<TransactionStatusObject>>
}