package payselection.demo

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import payselection.payments.sdk.PaySelectionPaymentsSdk
import payselection.payments.sdk.configuration.SdkConfiguration
import payselection.payments.sdk.models.requests.pay.*
import payselection.payments.sdk.ui.ThreeDsDialogFragment


class MainActivity : AppCompatActivity(), ThreeDsDialogFragment.ThreeDSDialogListener {

    lateinit var sdk: PaySelectionPaymentsSdk

    private val handler = CoroutineExceptionHandler { context, exception ->
        runOnUiThread {
            Toast.makeText(application, "Caught $exception", Toast.LENGTH_LONG).show()
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sdk = PaySelectionPaymentsSdk.getInstance(
            SdkConfiguration(
                "04bd07d3547bd1f90ddbd985feaaec59420cabd082ff5215f34fd1c89c5d8562e8f5e97a5df87d7c99bc6f16a946319f61f9eb3ef7cf355d62469edb96c8bea09e",
                "21044",
                true
            )
        )

        makePay("5260111696757102")
    }

    private fun getTransaction() {
        GlobalScope.launch(handler) {
            //Get this properties from PaymentResult
            val transactionId = "PS00000300026126"
            val transactionKey = "d58f99b6-6c6d-4186-9727-7ee5115ca288"
            testGetTransaction(transactionKey, transactionId)
        }
    }

    private fun makePay(cardNumber: String) {
        GlobalScope.launch(handler) {
            val orderId = "SAM_SDK_3"
            testPay(orderId, cardNumber)
        }
    }

    suspend fun testPay(orderId: String, cardNumber: String) {
        sdk.pay(
            orderId = orderId,
            description = "test payment",
            paymentData = PaymentData.createCrypto(
                transactionDetails = TransactionDetails(
                    amount = "10",
                    currency = "RUB"
                ),
//                tokenDetails = TokenDetails(
//                    payToken = "123"
//                )
                cardDetails = CardDetails(
                    cardholderName = "TEST CARD",
                    cardNumber = cardNumber,
                    cvc = "123",
                    expMonth = "12",
                    expYear = "24"
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
                println("Result $it")
                show3DS(it.redirectUrl)
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

    private fun show3DS(url: String) {
        // Открываем 3ds форму
        ThreeDsDialogFragment
            .newInstance(url)
            .show(supportFragmentManager, "3DS")
    }

    override fun onAuthorizationCompleted() {
        Toast.makeText(application, "Success", Toast.LENGTH_LONG).show()
    }

    override fun onAuthorizationFailed() {
        Toast.makeText(application, "Fail", Toast.LENGTH_LONG).show()
    }
}