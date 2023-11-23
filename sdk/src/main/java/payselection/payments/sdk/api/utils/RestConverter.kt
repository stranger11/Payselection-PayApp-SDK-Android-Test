package payselection.payments.sdk.api.utils

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import payselection.payments.sdk.api.models.TransactionStatusObject
import payselection.payments.sdk.models.requests.pay.CustomerInfo
import payselection.payments.sdk.models.requests.pay.TransactionDetails
import payselection.payments.sdk.models.requests.pay.enum.PaymentMethod
import payselection.payments.sdk.models.results.status.TransactionStatus

internal interface RestConverter {

    fun createTokenPayJson(
        orderId: String,
        description: String,
        paymentDetails: JsonElement?,
        transactionDetails: TransactionDetails,
        customerInfo: CustomerInfo?,
        paymentMethod: PaymentMethod,
        receiptData: JsonElement?,
        rebillFlag: Boolean?,
        extraData: JsonElement?
    ): JsonObject

    fun convertTransactions(list: List<TransactionStatusObject>): List<TransactionStatus>

    fun convertTransaction(data: TransactionStatusObject): TransactionStatus
}