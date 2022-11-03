package payselection.payments.sdk.models.requests.pay

import com.google.gson.annotations.SerializedName
import payselection.payments.sdk.models.requests.pay.enum.PaymentMethod
import java.util.concurrent.TimeUnit

data class PaymentData internal constructor(
    @SerializedName("TransactionDetails")
    val transactionDetails: TransactionDetails,
    @SerializedName("PaymentDetails")
    val cardDetails: CardDetails,
    @SerializedName("MessageExpiration")
    val messageExpiration: Long,
    @SerializedName("PaymentMethod")
    val paymentMethod: PaymentMethod
) {

    companion object {

        private val EXPIRATION = TimeUnit.DAYS.toMillis(1)

        fun create(transactionDetails: TransactionDetails, cardDetails: CardDetails): PaymentData {
            return PaymentData(
                transactionDetails = transactionDetails,
                cardDetails = cardDetails,
                messageExpiration = System.currentTimeMillis() + EXPIRATION,
                paymentMethod = PaymentMethod.Card
            )
        }
    }
}