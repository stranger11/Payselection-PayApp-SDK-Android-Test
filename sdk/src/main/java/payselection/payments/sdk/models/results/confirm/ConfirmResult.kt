package payselection.payments.sdk.models.results.confirm

import com.google.gson.annotations.SerializedName

data class ConfirmResult(
    @SerializedName("OrderId")
    val orderId: String,
    @SerializedName("TransactionId")
    val transactionId: String,
    @SerializedName("RebillId")
    val rebillId: String,
    @SerializedName("Amount")
    val amount: String,
    @SerializedName("Currency")
    val currency: String
)