package payselection.payments.sdk.models.results.status.sub

data class Details(
    val amount: String?,
    val currency: String?,
    val processingAmount: String?,
    val cryptoAmount: String?,
    val cryptoName: String?,
    val processingCurrency: String?,
    val remainingAmount: String?,
    val rebillId: String?
)