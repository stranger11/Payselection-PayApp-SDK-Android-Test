package payselection.payments.sdk.models.requests.pay

import com.google.gson.annotations.SerializedName

data class Client(
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("inn")
    val inn: String? = null,
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("phone")
    val phone: String? = null
)