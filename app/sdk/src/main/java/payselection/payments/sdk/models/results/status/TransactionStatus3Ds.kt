package payselection.payments.sdk.models.results.status

import payselection.payments.sdk.models.results.status.sub.Data3Ds

data class TransactionStatus3Ds(
    override val transactionId: String,
    override val transactionState: TransactionState,
    override val orderId: String,
    val data3Ds: Data3Ds
) : TransactionStatus