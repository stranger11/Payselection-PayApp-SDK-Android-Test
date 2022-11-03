package payselection.payments.sdk.models.results.status

import payselection.payments.sdk.models.results.status.sub.Redirect

data class TransactionStatusRedirect(
    override val transactionId: String,
    override val transactionState: TransactionState,
    override val orderId: String,
    val redirect: Redirect
) : TransactionStatus