package payselection.payments.sdk.models.requests.pay.enum

import com.google.gson.annotations.SerializedName

enum class VatType {
    @SerializedName("none")
    None,

    @SerializedName("vat0")
    Vat0,

    @SerializedName("vat10")
    Vat10,

    @SerializedName("vat110")
    Vat110,

    @SerializedName("vat20")
    Vat20,

    @SerializedName("vat120")
    Vat120
}