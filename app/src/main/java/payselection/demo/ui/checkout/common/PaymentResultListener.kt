package payselection.demo.ui.checkout.common

import payselection.payments.sdk.models.results.pay.PaymentResult

interface PaymentResultListener {
    fun onPaymentResult(result: PaymentResult?)
}