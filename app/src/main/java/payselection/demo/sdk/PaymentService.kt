package payselection.demo.sdk

import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import payselection.demo.models.Card
import payselection.demo.ui.checkout.common.PaymentResultListener
import payselection.payments.sdk.PaySelectionPaymentsSdk
import payselection.payments.sdk.configuration.SdkConfiguration
import payselection.payments.sdk.models.requests.pay.CardDetails
import payselection.payments.sdk.models.requests.pay.CustomerInfo
import payselection.payments.sdk.models.requests.pay.PaymentData
import payselection.payments.sdk.models.requests.pay.TransactionDetails
import payselection.payments.sdk.models.results.pay.PaymentResult

class PaymentService {

    private var sdk: PaySelectionPaymentsSdk? = null
    var paymentResult: PaymentResult? = null
    var card: Card? = null

    private var paymentResultListener: PaymentResultListener? = null

    fun init(sdkConfiguration: SdkConfiguration) {
        sdk = PaySelectionPaymentsSdk.getInstance(sdkConfiguration)
    }

    private val handler = CoroutineExceptionHandler { context, exception ->
        Log.e("SdkHelper", "Caught $exception")
    }

    fun pay(paymentCard: Card) {
        CoroutineScope(Dispatchers.IO).launch(handler) {
            card = paymentCard
            val orderId = "SAM_SDK_3"
            testPay(orderId, paymentCard)
        }
    }

    private suspend fun testPay(orderId: String, card: Card) {
        val dateParts = card.date.split('/')
        sdk?.pay(
            orderId = orderId,
            description = "test payment",
            paymentData = PaymentData.create(
                transactionDetails = TransactionDetails(
                    amount = "10",
                    currency = "RUB"
                ),
                cardDetails = CardDetails(
                    cardholderName = "TEST CARD",
                    cardNumber = card.number,
                    cvc = card.cvv.orEmpty(),
                    expMonth = dateParts[0],
                    expYear = dateParts[1]
                )
            ),
            customerInfo = CustomerInfo(
                email = "user@example.com",
                phone = "+19991231212",
                language = "en",
                address = "string",
                town = "string",
                zip = "string",
                country = "USA"
            ),
            rebillFlag = false
        )?.proceedResult(
            success = {
                paymentResult = it
                paymentResultListener?.onPaymentResult(it)
            },
            error = {
                paymentResultListener?.onPaymentResult(null)
                it.printStackTrace()
            }
        )
    }

    fun getTransaction() {
        CoroutineScope(Dispatchers.IO).launch(handler) {
            //Get this properties from PaymentResult
            val transactionId = paymentResult?.transactionId
            val transactionKey = paymentResult?.transactionSecretKey
            testGetTransaction(transactionKey.orEmpty(), transactionId.orEmpty())
        }
    }

    private suspend fun testGetTransaction(transactionKey: String, transactionId: String) {
        sdk?.getTransaction(transactionKey, transactionId)?.proceedResult(
            success = {
                println("Result $it")
            },
            error = {
                it.printStackTrace()
            }
        )
    }

    companion object {
        private var instance: PaymentService? = null

        fun getInstance(paymentResultListener: PaymentResultListener? = null): PaymentService {
            if (instance == null) {
                instance = PaymentService()
            }
            if (paymentResultListener != null) instance?.paymentResultListener = paymentResultListener
            return instance ?: throw IllegalStateException("Unable to create instance.")
        }
    }
}