package payselection.payments.sdk.models.requests.pay

import com.google.gson.annotations.SerializedName

data class ReceiptData(
    @SerializedName("timestamp")
    val timestamp: String,
    @SerializedName("external_id")
    val externalId: String? = null,
    @SerializedName("receipt")
    val receipt: Receipt
)