package payselection.payments.sdk.models.requests.pay

import com.google.gson.annotations.SerializedName

data class CustomerInfo(
    @SerializedName("Email")
    val email: String? = null,
    @SerializedName("ReceiptEmail")
    val receiptEmail: String? = null,
    @SerializedName("IsSendReceipt")
    val isSendReceipt: Boolean? = null,
    @SerializedName("Phone")
    val phone: String? = null,
    @SerializedName("Language")
    val language: String? = null,
    @SerializedName("Address")
    val address: String? = null,
    @SerializedName("Town")
    val town: String? = null,
    @SerializedName("ZIP")
    val zip: String? = null,
    @SerializedName("Country")
    val country: String? = null
) {
    @SerializedName("IP")
    internal var ip: String = ""
}