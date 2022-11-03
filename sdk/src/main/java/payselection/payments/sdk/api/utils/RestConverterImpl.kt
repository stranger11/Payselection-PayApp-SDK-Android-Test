package payselection.payments.sdk.api.utils

import com.google.gson.JsonObject
import payselection.payments.sdk.api.models.TransactionStatusObject
import payselection.payments.sdk.models.requests.pay.TransactionDetails
import payselection.payments.sdk.models.requests.pay.enum.PaymentDetailsType
import payselection.payments.sdk.models.requests.pay.enum.PaymentMethod
import payselection.payments.sdk.models.results.status.*
import payselection.payments.sdk.models.results.status.sub.Data3Ds
import payselection.payments.sdk.models.results.status.sub.Details
import payselection.payments.sdk.models.results.status.sub.Error
import payselection.payments.sdk.models.results.status.sub.Redirect
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.*

internal class RestConverterImpl() : RestConverter {

    override fun createTokenPayJson(orderId: String, description: String, token: String, transactionDetails: TransactionDetails): JsonObject {
        val jsonObject = JsonObject()
        jsonObject.addProperty("OrderId", orderId)
        jsonObject.addProperty("Amount", transactionDetails.amount)
        jsonObject.addProperty("Currency", transactionDetails.currency)
        jsonObject.add("CustomerInfo", JsonObject().apply {
            addProperty("IP", getIPAddress())
        })
        jsonObject.addProperty("Description", description)
        jsonObject.addProperty("PaymentMethod", PaymentMethod.Token.name)
        jsonObject.add("PaymentDetails", JsonObject().apply {
            addProperty("Type", PaymentDetailsType.Internal.name)
            addProperty("PayToken", token)
        })
        return jsonObject
    }

    override fun convertTransactions(list: List<TransactionStatusObject>): List<TransactionStatus> {
        return list.map(::convertTransaction)
    }

    override fun convertTransaction(data: TransactionStatusObject): TransactionStatus {
        return when (val state = TransactionState.convert(data.TransactionState)) {
            TransactionState.Success,
            TransactionState.Preauthorized,
            TransactionState.Pending,
            TransactionState.Voided -> TransactionStatusDetails(
                transactionId = data.TransactionId,
                transactionState = state,
                orderId = data.OrderId,
                details = Details(
                    amount = data.StateDetails.Amount,
                    currency = data.StateDetails.Currency,
                    processingAmount = data.StateDetails.ProcessingAmount,
                    cryptoAmount = data.StateDetails.CryptoAmount,
                    cryptoName = data.StateDetails.CryptoName,
                    processingCurrency = data.StateDetails.ProcessingCurrency,
                    remainingAmount = data.StateDetails.RemainingAmount,
                    rebillId = data.StateDetails.RebillId
                )
            )
            TransactionState.Declined -> TransactionStatusError(
                transactionId = data.TransactionId,
                transactionState = state,
                orderId = data.OrderId,
                error = Error(
                    code = data.StateDetails.Code,
                    description = data.StateDetails.Description
                )
            )
            TransactionState.WaitFor3ds -> TransactionStatus3Ds(
                transactionId = data.TransactionId,
                transactionState = state,
                orderId = data.OrderId,
                data3Ds = Data3Ds(
                    acsUrl = data.StateDetails.AcsUrl,
                    paReq = data.StateDetails.PaReq,
                    MD = data.StateDetails.MD
                )
            )
            TransactionState.Redirect -> TransactionStatusRedirect(
                transactionId = data.TransactionId,
                transactionState = state,
                orderId = data.OrderId,
                redirect = Redirect(
                    redirectUrl = data.StateDetails.RedirectUrl,
                    redirectMethod = data.StateDetails.RedirectMethod
                )
            )
            TransactionState.Unknown -> TransactionStatusBase(
                transactionId = data.TransactionId,
                transactionState = state,
                orderId = data.OrderId
            )
        }
    }

    companion object {

        fun getIPAddress(): String {
            try {
                val interfaces: List<NetworkInterface> = Collections.list(NetworkInterface.getNetworkInterfaces())
                for (intf in interfaces) {
                    val addrs: List<InetAddress> = Collections.list(intf.inetAddresses)
                    for (addr in addrs) {
                        if (!addr.isLoopbackAddress && addr is Inet4Address) {
                            return addr.hostAddress.orEmpty()
                        }
                    }
                }
            } catch (ignored: Exception) {
            }
            return ""
        }
    }
}