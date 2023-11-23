package payselection.payments.sdk.models.requests.pay

import com.google.gson.annotations.SerializedName
import payselection.payments.sdk.models.requests.pay.enum.PaymentMethod
import java.util.concurrent.TimeUnit

data class PaymentData internal constructor(
    @SerializedName("TransactionDetails")
    val transactionDetails: TransactionDetails,
    @SerializedName("PaymentDetails")
    val cardDetails: CardDetails? = null,
    @SerializedName("TokenDetails")
    val tokenDetails: TokenDetails? = null,
    @SerializedName("MessageExpiration")
    val messageExpiration: Long? = null,
    @SerializedName("PaymentMethod")
    val paymentMethod: PaymentMethod
) {

    companion object {

        private val EXPIRATION = TimeUnit.DAYS.toMillis(1)

        fun createCrypto(transactionDetails: TransactionDetails, cardDetails: CardDetails): PaymentData {
            return PaymentData(
                transactionDetails = transactionDetails,
                cardDetails = cardDetails,
                messageExpiration = System.currentTimeMillis() + EXPIRATION,
                paymentMethod = PaymentMethod.Cryptogram
            )
        }

        fun createToken(transactionDetails: TransactionDetails, tokenDetails: TokenDetails): PaymentData {
            return PaymentData(
                transactionDetails = transactionDetails,
                tokenDetails = tokenDetails,
                paymentMethod = PaymentMethod.Token
            )
        }

        fun createQr(transactionDetails: TransactionDetails): PaymentData {
            return PaymentData(
                transactionDetails = transactionDetails,
                paymentMethod = PaymentMethod.QR
            )
        }
    }
}