package payselection.payments.sdk.models.requests.pay

import com.google.gson.annotations.SerializedName

data class CardDetails(
    @SerializedName("CardholderName")
    val cardholderName: String,
    @SerializedName("CardNumber")
    val cardNumber: String,
    @SerializedName("CVC")
    val cvc: String,
    @SerializedName("ExpMonth")
    val expMonth: String,
    @SerializedName("ExpYear")
    val expYear: String
)