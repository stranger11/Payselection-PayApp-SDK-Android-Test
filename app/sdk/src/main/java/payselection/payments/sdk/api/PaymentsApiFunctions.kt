package payselection.payments.sdk.api

import com.google.gson.JsonObject
import kotlinx.coroutines.Deferred
import payselection.payments.sdk.api.models.TransactionStatusObject
import payselection.payments.sdk.models.results.confirm.ConfirmResult
import payselection.payments.sdk.models.results.pay.PaymentResult
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

internal interface PaymentsApiFunctions {

    @POST("/payments/requests/single")
    fun pay(@Body data: JsonObject): Deferred<Response<PaymentResult>>

    @GET("/orders/{orderId}")
    fun getOrderStatus(@Path("orderId") orderId: String): Deferred<Response<List<TransactionStatusObject>>>

    @GET("/transactions/{transactionId}")
    fun getTransaction(@Path("transactionId") transactionId: String): Deferred<Response<TransactionStatusObject>>

    @POST("/payments/refund")
    fun refund(@Body data: JsonObject): Deferred<Response<PaymentResult>>

    @POST("/payments/confirmation")
    fun confirm(@Body data: JsonObject): Deferred<Response<ConfirmResult>>
}