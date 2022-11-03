package payselection.payments.sdk.api.models

import com.google.gson.annotations.SerializedName

internal class ServerError(
    @SerializedName("Code")
    val code: String?,
    @SerializedName("Description")
    val description: String?
)