package payselection.demo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import payselection.payments.sdk.PaySelectionPaymentsSdk
import payselection.payments.sdk.configuration.SdkConfiguration
import payselection.payments.sdk.models.requests.pay.CardDetails
import payselection.payments.sdk.models.requests.pay.PaymentData
import payselection.payments.sdk.models.requests.pay.TransactionDetails


class MainActivity : AppCompatActivity() {

    lateinit var sdk: PaySelectionPaymentsSdk

    private val handler = CoroutineExceptionHandler { context, exception ->
        Toast.makeText(application, "Caught $exception", Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sdk = PaySelectionPaymentsSdk.getInstance(
            SdkConfiguration(
                "04a36ce5163f6120972a6bf46a76600953ce252e8d513e4eea1f097711747e84a2b7bf967a72cf064fedc171f5effda2b899e8c143f45303c9ee68f7f562951c88",
                "20337",
                true
            )
        )

        makePay()
    }

    private fun getTransaction() {
        GlobalScope.launch(handler) {
            //Get this properties from PaymentResult
            val transactionId = "PS00000300026126"
            val transactionKey = "d58f99b6-6c6d-4186-9727-7ee5115ca288"
            testGetTransaction(transactionKey, transactionId)
        }
    }

    private fun makePay() {
        GlobalScope.launch(handler) {
            val orderId = "234574"
            testPay(orderId)
        }
    }

    suspend fun testPay(orderId: String) {
        sdk.pay(
            orderId = orderId,
            description = "test payment",
            paymentData = PaymentData.create(
                transactionDetails = TransactionDetails(
                    amount = "100",
                    currency = "RUB"
                ),
                cardDetails = CardDetails(
                    cardholderName = "TEST CARD",
                    cardNumber = "4111111111111111",
                    cvc = "123",
                    expMonth = "12",
                    expYear = "24"
                )
            )
        ).proceedResult(
            success = {
                println("Result $it")
            },
            error = {
                it.printStackTrace()
            }
        )
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