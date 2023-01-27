package payselection.payments.sdk.api

import com.google.gson.JsonObject
import payselection.payments.sdk.api.models.TransactionStatusObject
import payselection.payments.sdk.models.results.pay.PaymentResult
import payselection.payments.sdk.utils.Result

internal interface PaymentsRestApi {

    suspend fun pay(data: JsonObject): Result<PaymentResult>

    suspend fun getTransaction(transactionKey: String, transactionId: String): Result<TransactionStatusObject>
}