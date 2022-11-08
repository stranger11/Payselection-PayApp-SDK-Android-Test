package payselection.payments.sdk.api.models

internal class StateDetailsObject(
    val Amount: String?,
    val Currency: String?,
    val ProcessingAmount: String?,
    val CryptoAmount: String?,
    val CryptoName: String?,
    val ProcessingCurrency: String?,
    val RemainingAmount: String?,
    val RebillId: String?,
    val Code: String?,
    val Description: String?,
    val AcsUrl: String?,
    val PaReq: String?,
    val MD: String?,
    val RedirectUrl: String?,
    val RedirectMethod: String?
) {
}