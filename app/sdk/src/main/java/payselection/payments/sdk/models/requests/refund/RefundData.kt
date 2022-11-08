package payselection.payments.sdk.models.requests.refund

import com.google.gson.annotations.SerializedName

data class RefundData(
    @SerializedName("TransactionId")
    val transactionId: String,
    @SerializedName("Amount")
    val amount: String,
    @SerializedName("Currency")
    val currency: String
)