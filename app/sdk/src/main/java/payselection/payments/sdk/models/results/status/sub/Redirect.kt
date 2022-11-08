package payselection.payments.sdk.models.results.status.sub

data class Redirect(
    val redirectUrl: String?,
    val redirectMethod: String?
)