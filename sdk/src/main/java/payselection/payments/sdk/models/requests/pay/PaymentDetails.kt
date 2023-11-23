package payselection.payments.sdk.models.requests.pay

import com.google.gson.annotations.SerializedName

interface PaymentDetails

data class PaymentDetailsToken(
    @SerializedName("PayToken")
    val payToken: String
) : PaymentDetails {
    @SerializedName("Type")
    val type: String = "Yandex"
}

data class PaymentDetailsCryptogram(
    @SerializedName("Value")
    val value: String
) : PaymentDetails