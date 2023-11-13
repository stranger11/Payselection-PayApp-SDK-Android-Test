package payselection.demo.sdk

import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
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

class PaymentHelper(private var paymentResultListener: PaymentResultListener) {

    private lateinit var sdk: PaySelectionPaymentsSdk

    fun init(sdkConfiguration: SdkConfiguration) {
        sdk = PaySelectionPaymentsSdk.getInstance(sdkConfiguration)
    }

    private val handler = CoroutineExceptionHandler { context, exception ->
        Log.e("SdkHelper", "Caught $exception")
    }

    fun pay(card: Card) {
        GlobalScope.launch(handler) {
            val orderId = "SAM_SDK_3"
            testPay(orderId, card)
        }
    }

    private suspend fun testPay(orderId: String, card: Card) {
        val dateParts = card.date.split('/')
        sdk.pay(
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
        ).proceedResult(
            success = {
                paymentResultListener.onPaymentResult(it)
            },
            error = {
                it.printStackTrace()
            }
        )
    }

    private fun getTransaction() {
        GlobalScope.launch(handler) {
            //Get this properties from PaymentResult
            val transactionId = "PS00000300026126"
            val transactionKey = "d58f99b6-6c6d-4186-9727-7ee5115ca288"
            testGetTransaction(transactionKey, transactionId)
        }
    }

    suspend fun testGetTransaction(transactionKey: String, transactionId: String) {
        sdk.getTransaction(transactionKey, transactionId).proceedResult(
            success = {
                println("Result $it")
            },
            error = {
                it.printStackTrace()
            }
        )
    }
}