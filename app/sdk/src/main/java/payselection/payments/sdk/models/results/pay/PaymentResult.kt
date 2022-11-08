package payselection.payments.sdk.models.results.pay

import com.google.gson.annotations.SerializedName

data class PaymentResult(
    @SerializedName("OrderId")
    val orderId: String,
    @SerializedName("TransactionId")
    val transactionId: String,
    @SerializedName("Amount")
    val amount: String,
    @SerializedName("Currency")
    val currency: String
)