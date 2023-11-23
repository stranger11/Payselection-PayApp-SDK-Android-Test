package payselection.payments.sdk

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import payselection.payments.sdk.api.PaymentsRestClient
import payselection.payments.sdk.api.utils.RestConverterImpl
import payselection.payments.sdk.configuration.SdkConfiguration
import payselection.payments.sdk.crypto.CryptoModule
import payselection.payments.sdk.models.requests.pay.CustomerInfo
import payselection.payments.sdk.models.requests.pay.ExtraData
import payselection.payments.sdk.models.requests.pay.PaymentData
import payselection.payments.sdk.models.requests.pay.PaymentDetailsCryptogram
import payselection.payments.sdk.models.requests.pay.PaymentDetailsToken
import payselection.payments.sdk.models.requests.pay.ReceiptData
import payselection.payments.sdk.models.requests.pay.enum.PaymentMethod
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

    override suspend fun pay(
        orderId: String,
        paymentData: PaymentData,
        description: String,
        rebillFlag: Boolean?,
        customerInfo: CustomerInfo,
        extraData: ExtraData?,
        receiptData: ReceiptData?
    ): Result<PaymentResult> {
        val paymentDetails = when (paymentData.paymentMethod) {
            PaymentMethod.Cryptogram -> {
                val paymentDataString = gson.toJson(paymentData).toString()
                PaymentDetailsCryptogram(CryptoModule.createCryptogram(paymentDataString, configuration.publicKey))
            }

            PaymentMethod.Token -> {
                PaymentDetailsToken(paymentData.tokenDetails?.payToken ?: "")
            }

            PaymentMethod.QR -> null
        }
        return restClient.pay(
            restConverter.createTokenPayJson(
                orderId = orderId,
                description = description,
                paymentDetails = gson.toJsonTree(paymentDetails),
                transactionDetails = paymentData.transactionDetails,
                customerInfo = customerInfo,
                paymentMethod = paymentData.paymentMethod,
                receiptData = gson.toJsonTree(receiptData),
                rebillFlag = rebillFlag,
                extraData = gson.toJsonTree(extraData)
            )
        )
    }

    override suspend fun getTransaction(transactionKey: String, transactionId: String): Result<TransactionStatus> {
        return restClient.getTransaction(transactionKey, transactionId).convertResult {
            restConverter.convertTransaction(it)
        }
    }

    private fun <T : Any> T.toJsonObject(): JsonObject {
        return jsonParser.parse(gson.toJson(this)).asJsonObject
    }
}