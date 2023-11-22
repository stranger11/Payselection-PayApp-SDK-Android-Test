package payselection.payments.sdk.models.requests.pay

import com.google.gson.annotations.SerializedName

data class AgentInfo(
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("paying_agent")
    val payingAgent: PayingAgent? = null,
    @SerializedName("receive_payments_operator")
    val receivePaymentsOperator: ReceivePaymentsOperator? = null,
    @SerializedName("money_transfer_operator")
    val moneyTransferOperator: MoneyTransferOperator? = null
)

data class PayingAgent(
    @SerializedName("operation")
    val operation: String? = null,
    @SerializedName("phones")
    val phones: List<String>? = null
)

data class ReceivePaymentsOperator(
    @SerializedName("phones")
    val phones: List<String>? = null
)

data class MoneyTransferOperator(
    @SerializedName("phones")
    val phones: List<String>? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("address")
    val address: String? = null,
    @SerializedName("inn")
    val inn: String? = null
)