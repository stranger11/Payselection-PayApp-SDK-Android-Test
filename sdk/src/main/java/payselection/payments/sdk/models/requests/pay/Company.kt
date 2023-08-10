package payselection.payments.sdk.models.requests.pay

import com.google.gson.annotations.SerializedName

data class Company(
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("sno")
    val sno: String? = null,
    @SerializedName("inn")
    val inn: String,
    @SerializedName("payment_address")
    val paymentAddress: String
)