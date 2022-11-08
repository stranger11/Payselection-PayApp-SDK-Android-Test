package payselection.payments.sdk

import payselection.payments.sdk.configuration.SdkConfiguration
import payselection.payments.sdk.models.requests.confirm.ConfirmData
import payselection.payments.sdk.models.requests.pay.PaymentData
import payselection.payments.sdk.models.requests.refund.RefundData
import payselection.payments.sdk.models.results.confirm.ConfirmResult
import payselection.payments.sdk.models.results.pay.PaymentResult
import payselection.payments.sdk.models.results.status.TransactionStatus
import payselection.payments.sdk.utils.Result

interface PaySelectionPaymentsSdk {

    suspend fun pay(orderId: String, paymentData: PaymentData, description: String = ""): Result<PaymentResult>

    suspend fun getTransaction(transactionId: String): Result<TransactionStatus>

    suspend fun getOrderStatus(orderId: String): Result<List<TransactionStatus>>

    suspend fun refund(refundData: RefundData): Result<PaymentResult>

    suspend fun confirm(confirmData: ConfirmData): Result<ConfirmResult>

    companion object {

        fun getInstance(configuration: SdkConfiguration): PaySelectionPaymentsSdk {
            return PaymentSdkImpl(configuration)
        }
    }
}