package payselection.payments.sdk.api.models

internal class TransactionStatusObject(
    val TransactionState: String,
    val TransactionId: String,
    val OrderId: String,
    val StateDetails: StateDetailsObject
) {
}