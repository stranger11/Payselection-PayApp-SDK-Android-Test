package payselection.payments.sdk.models.requests.pay

import com.google.gson.annotations.SerializedName

data class TransactionDetails(
    @SerializedName("Amount")
    val amount: String,
    @SerializedName("Currency")
    val currency: String
)