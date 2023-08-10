package payselection.payments.sdk.models.requests.pay

import com.google.gson.annotations.SerializedName
import payselection.payments.sdk.models.requests.pay.enum.ReceiptPaymentMethod
import payselection.payments.sdk.models.requests.pay.enum.ReceiptPaymentObject
import payselection.payments.sdk.models.requests.pay.enum.VatType
import java.math.BigDecimal

data class Receipt(
    @SerializedName("client")
    val client: Client,
    @SerializedName("company")
    val company: Company,
    @SerializedName("agent_info")
    val agentInfo: AgentInfo? = null,
    @SerializedName("supplier_info")
    val supplierInfo: SupplierInfo? = null,
    @SerializedName("items")
    val items: List<Item>,
    @SerializedName("payments")
    val payments: List<Payment>,
    @SerializedName("vats")
    val vats: List<Vat>? = null,
    @SerializedName("total")
    val total: BigDecimal,
    @SerializedName("additional_check_props")
    val additionalCheckProps: String? = null,
    @SerializedName("cashier")
    val cashier: String? = null,
    @SerializedName("additional_user_props")
    val additionalUserProps: AdditionalUserProps? = null,
    @SerializedName("operating_check_props")
    val operatingCheckProps: OperatingCheckProps? = null,
    @SerializedName("sectoral_check_props")
    val sectoralCheckProps: SectoralCheckProps? = null
)

data class Item(
    @SerializedName("name")
    val name: String,
    @SerializedName("price")
    val price: BigDecimal,
    @SerializedName("quantity")
    val quantity: BigDecimal,
    @SerializedName("sum")
    val sum: String?,
    @SerializedName("measurement_unit")
    val measurementUnit: String? = null,
    @SerializedName("payment_method")
    val paymentMethod: ReceiptPaymentMethod,
    @SerializedName("payment_object")
    val paymentObject: ReceiptPaymentObject,
    @SerializedName("nomenclature_code")
    val nomenclatureCode: String? = null,
    @SerializedName("vat")
    val vat: Vat,
    @SerializedName("agent_info")
    val agentInfo: AgentInfo? = null,
    @SerializedName("supplier_info")
    val supplierInfo: SupplierInfo? = null,
    @SerializedName("user_data")
    val userData: String? = null,
    @SerializedName("excise")
    val excise: BigDecimal? = null,
    @SerializedName("country_code")
    val countryCode: String? = null,
    @SerializedName("declaration_number")
    val declarationCode: String? = null
)

data class Vat(
    @SerializedName("type")
    val type: VatType,
    @SerializedName("sum")
    val sum: BigDecimal? = null
)

data class Payment(
    @SerializedName("type")
    val type: String,
    @SerializedName("sum")
    val sum: String
)

data class AdditionalUserProps(
    @SerializedName("name")
    val name: String,
    @SerializedName("value")
    val value: String
)

data class OperatingCheckProps(
    @SerializedName("name")
    val name: String,
    @SerializedName("value")
    val value: String,
    @SerializedName("timestamp")
    val timestamp: String
)

data class SectoralCheckProps(
    val props: List<SectoralProp>
)

data class SectoralProp(
    @SerializedName("federal_id")
    val federalId: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("number")
    val number: String,
    @SerializedName("value")
    val value: String
)

data class SupplierInfo(
    @SerializedName("phones")
    val phones: List<String>? = null
)