package payselection.payments.sdk.api

import com.google.gson.JsonObject
import payselection.payments.sdk.api.models.TransactionStatusObject
import payselection.payments.sdk.models.results.confirm.ConfirmResult
import payselection.payments.sdk.models.results.pay.PaymentResult
import payselection.payments.sdk.utils.Result

internal interface PaymentsRestApi {

    suspend fun pay(data: JsonObject): Result<PaymentResult>

    suspend fun getOrderStatus(orderId: String): Result<List<TransactionStatusObject>>

    suspend fun getTransaction(transactionId: String): Result<TransactionStatusObject>

    suspend fun refund(data: JsonObject): Result<PaymentResult>

    suspend fun confirm(data: JsonObject): Result<ConfirmResult>
}