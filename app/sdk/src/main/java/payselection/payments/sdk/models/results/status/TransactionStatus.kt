package payselection.payments.sdk.models.results.status

interface TransactionStatus {
    val transactionId: String
    val transactionState: TransactionState
    val orderId: String
}