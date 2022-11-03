package payselection.payments.sdk

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import payselection.payments.sdk.api.PaymentsRestClient
import payselection.payments.sdk.api.utils.RestConverterImpl
import payselection.payments.sdk.configuration.SdkConfiguration
import payselection.payments.sdk.crypto.CryptoModule
import payselection.payments.sdk.models.requests.confirm.ConfirmData
import payselection.payments.sdk.models.requests.pay.PaymentData
import payselection.payments.sdk.models.requests.refund.RefundData
import payselection.payments.sdk.models.results.confirm.ConfirmResult
import payselection.payments.sdk.models.results.pay.PaymentResult
import payselection.payments.sdk.models.results.status.TransactionStatus
import payselection.payments.sdk.utils.Result

internal class PaymentSdkImpl(
    private val configuration: SdkConfiguration
) : PaySelectionPaymentsSdk {

    private val restClient = PaymentsRestClient(configuration)
    private val restConverter = RestConverterImpl()

    private val gson = Gson()
    private val jsonParser = JsonParser()

    override suspend fun pay(orderId: String, paymentData: PaymentData, description: String): Result<PaymentResult> {
        val paymentDataString = gson.toJson(paymentData).toString()
        val token = CryptoModule.createCryptogram(paymentDataString, configuration.publicKey)
        return restClient.pay(
            restConverter.createTokenPayJson(
                orderId = orderId,
                description = description,
                token = token,
                transactionDetails = paymentData.transactionDetails
            )
        )
    }

    override suspend fun getTransaction(transactionId: String): Result<TransactionStatus> {
        return restClient.getTransaction(transactionId).convertResult {
            restConverter.convertTransaction(it)
        }
    }

    override suspend fun getOrderStatus(orderId: String): Result<List<TransactionStatus>> {
        return restClient.getOrderStatus(orderId).convertResult {
            restConverter.convertTransactions(it)
        }
    }

    override suspend fun refund(refundData: RefundData): Result<PaymentResult> {
        return restClient.refund(refundData.toJsonObject())
    }

    override suspend fun confirm(confirmData: ConfirmData): Result<ConfirmResult> {
        return restClient.confirm(confirmData.toJsonObject())
    }

    private fun <T : Any> T.toJsonObject(): JsonObject {
        return jsonParser.parse(gson.toJson(this)).asJsonObject
    }
}