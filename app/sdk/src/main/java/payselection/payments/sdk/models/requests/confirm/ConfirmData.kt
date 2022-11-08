package payselection.payments.sdk.models.requests.confirm

import com.google.gson.annotations.SerializedName

data class ConfirmData(
    @SerializedName("OrderId")
    val orderId: String,
    @SerializedName("TransactionId")
    val transactionId: String,
    @SerializedName("PaRes")
    val paRes: String,
    @SerializedName("MD")
    val MD: String
)