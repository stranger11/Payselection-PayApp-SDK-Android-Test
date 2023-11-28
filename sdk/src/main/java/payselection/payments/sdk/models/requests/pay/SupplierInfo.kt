package payselection.payments.sdk.models.requests.pay

import com.google.gson.annotations.SerializedName

data class SupplierInfo(
    @SerializedName("phones")
    val phones: List<String>? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("inn")
    val inn: String? = null
)