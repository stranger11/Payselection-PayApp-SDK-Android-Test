package payselection.demo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import payselection.payments.sdk.PaySelectionPaymentsSdk
import payselection.payments.sdk.configuration.SdkConfiguration
import payselection.payments.sdk.models.requests.confirm.ConfirmData
import payselection.payments.sdk.models.requests.pay.CardDetails
import payselection.payments.sdk.models.requests.pay.PaymentData
import payselection.payments.sdk.models.requests.pay.TransactionDetails
import payselection.payments.sdk.models.requests.refund.RefundData

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
                "046fdb81fc698b90dd12f005bc399208fd01bb3225e962d58e115c86d905c5f2144cb5dfe2a30868fdf165a5010de46235a248c645b657c046038466537b01f1d6",
                "20160",
                "5ve4wkzTycthTKut",
                true
            )
        )

        GlobalScope.launch(handler) {
            val orderId = "234574"
            val transactionId = "PS00000300026126"
            testPay(orderId)
//            testGetOrderStatus(orderId)
//            testGetTransaction(transactionId)
//            testRefund(transactionId)
//            testRefund(orderId, transactionId)
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

    suspend fun testGetOrderStatus(orderId: String) {
        sdk.getOrderStatus(orderId).proceedResult(
            success = {
                println("Result $it")
            },
            error = {
                it.printStackTrace()
            }
        )
    }

    suspend fun testGetTransaction(transactionId: String) {
        sdk.getTransaction(transactionId).proceedResult(
            success = {
                println("Result $it")
            },
            error = {
                it.printStackTrace()
            }
        )
    }

    suspend fun testRefund(transactionId: String) {
        sdk.refund(
            refundData = RefundData(
                transactionId = transactionId,
                amount = "100",
                currency = "RUB"
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

    suspend fun testConfirm(orderId: String, transactionId: String) {
        sdk.confirm(
            confirmData = ConfirmData(
                transactionId = transactionId,
                orderId = orderId,
                paRes = "string",
                MD = "string"
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
}