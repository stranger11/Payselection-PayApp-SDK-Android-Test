package payselection.payments.sdk.models.results.status

import payselection.payments.sdk.models.results.status.sub.Error

data class TransactionStatusError(
    override val transactionId: String,
    override val transactionState: TransactionState,
    override val orderId: String,
    val error: Error
) : TransactionStatus