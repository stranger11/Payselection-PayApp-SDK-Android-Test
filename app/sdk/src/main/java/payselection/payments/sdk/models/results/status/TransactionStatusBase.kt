package payselection.payments.sdk.models.results.status

data class TransactionStatusBase(
    override val transactionId: String,
    override val transactionState: TransactionState,
    override val orderId: String,
) : TransactionStatus