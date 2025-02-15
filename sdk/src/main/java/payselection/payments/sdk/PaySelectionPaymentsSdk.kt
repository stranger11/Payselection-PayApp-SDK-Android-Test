package payselection.payments.sdk

import payselection.payments.sdk.configuration.SdkConfiguration
import payselection.payments.sdk.models.requests.pay.CustomerInfo
import payselection.payments.sdk.models.requests.pay.PaymentData
import payselection.payments.sdk.models.requests.pay.ReceiptData
import payselection.payments.sdk.models.results.pay.PaymentResult
import payselection.payments.sdk.models.results.status.TransactionStatus
import payselection.payments.sdk.utils.Result

interface PaySelectionPaymentsSdk {

    suspend fun pay(
        orderId: String,
        paymentData: PaymentData,
        description: String = "",
        customerInfo: CustomerInfo? = null,
        receiptData: ReceiptData? = null,
        rebillFlag: Boolean? = null,
    ): Result<PaymentResult>

    suspend fun getTransaction(transactionKey: String, transactionId: String): Result<TransactionStatus>

    companion object {

        fun getInstance(configuration: SdkConfiguration): PaySelectionPaymentsSdk {
            return PaymentSdkImpl(configuration)
        }
    }
}