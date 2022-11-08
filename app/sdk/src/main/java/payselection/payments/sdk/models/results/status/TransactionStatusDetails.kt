package payselection.payments.sdk.models.results.status

import payselection.payments.sdk.models.results.status.sub.Details

data class TransactionStatusDetails(
    override val transactionId: String,
    override val transactionState: TransactionState,
    override val orderId: String,
    val details: Details
) : TransactionStatus