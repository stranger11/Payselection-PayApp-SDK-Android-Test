package payselection.payments.sdk.models.requests.pay

import com.google.gson.annotations.SerializedName
data class ExtraData(
    @SerializedName("ReturnUrl")
    val returnUrl: String? = null,
    @SerializedName("WebhookUrl")
    val webhookUrl: String? = null,
    @SerializedName("ScreenHeight")
    val screenHeight: String? = null,
    @SerializedName("ScreenWidth")
    val screenWidth: String? = null,
    @SerializedName("ChallengeWindowSize")
    val challengeWindowSize: String? = null,
    @SerializedName("TimeZoneOffset")
    val timeZoneOffset: String? = null,
    @SerializedName("ColorDepth")
    val colorDepth: String? = null,
    @SerializedName("Region")
    val region: String? = null,
    @SerializedName("UserAgent")
    val userAgent: String? = null,
    @SerializedName("acceptHeader")
    val acceptHeader: String? = null,
    @SerializedName("JavaEnabled")
    val javaEnabled: Boolean? = null,
    @SerializedName("javaScriptEnabled")
    val javaScriptEnabled: Boolean? = null
)