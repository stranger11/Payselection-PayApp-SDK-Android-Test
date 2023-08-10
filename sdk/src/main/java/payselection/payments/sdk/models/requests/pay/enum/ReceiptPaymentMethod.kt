package payselection.payments.sdk.models.requests.pay.enum

import com.google.gson.annotations.SerializedName

enum class ReceiptPaymentMethod {
    @SerializedName("full_prepayment")
    FullPrepayment,

    @SerializedName("prepayment")
    Prepayment,

    @SerializedName("advance")
    Advance,

    @SerializedName("full_payment")
    FullPayment,

    @SerializedName("partial_payment")
    PartialPayment,

    @SerializedName("credit_payment")
    CreditPayment,
}