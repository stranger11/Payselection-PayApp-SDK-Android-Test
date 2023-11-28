package payselection.payments.sdk.models.requests.pay

import com.google.gson.annotations.SerializedName
import payselection.payments.sdk.models.requests.pay.enum.ReceiptPaymentMethod
import payselection.payments.sdk.models.requests.pay.enum.ReceiptPaymentObject
import payselection.payments.sdk.models.requests.pay.enum.VatType

sealed class Receipt {
    data class Receipt_ffd05(
        @SerializedName("client")
        val client: Client,
        @SerializedName("company")
        val company: Company,
        @SerializedName("agent_info")
        val agentInfo: AgentInfo? = null,
        @SerializedName("supplier_info")
        val supplierInfo: SupplierInfo? = null,
        @SerializedName("items")
        val items: List<Item.Item_ffd05>,
        @SerializedName("payments")
        val payments: List<Payment>,
        @SerializedName("vats")
        val vats: List<Vat>? = null,
        @SerializedName("total")
        val total: Double,
        @SerializedName("additional_check_props")
        val additionalCheckProps: String? = null,
        @SerializedName("cashier")
        val cashier: String? = null,
        @SerializedName("additional_user_props")
        val additionalUserProps: AdditionalUserProps? = null,
    ) : Receipt()

    data class Receipt_ffd2(
        @SerializedName("client")
        val client: Client,
        @SerializedName("company")
        val company: Company,
        @SerializedName("items")
        val items: List<Item.Item_ffd2>,
        @SerializedName("payments")
        val payments: List<Payment>,
        @SerializedName("vats")
        val vats: List<Vat>? = null,
        @SerializedName("total")
        val total: Double,
        @SerializedName("additional_check_props")
        val additionalCheckProps: String? = null,
        @SerializedName("cashier")
        val cashier: String? = null,
        @SerializedName("additional_user_props")
        val additionalUserProps: AdditionalUserProps? = null,
        @SerializedName("operating_check_props")
        val operatingCheckProps: OperatingCheckProps? = null,
        @SerializedName("sectoral_check_props")
        val sectoralCheckProps: List<SectoralProp>? = null
    ) : Receipt()
}

sealed class Item {
    data class Item_ffd05(
        @SerializedName("name")
        val name: String,
        @SerializedName("price")
        val price: Double,
        @SerializedName("quantity")
        val quantity: Double,
        @SerializedName("sum")
        val sum: Double?,
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
        val excise: Double? = null,
        @SerializedName("country_code")
        val countryCode: String? = null,
        @SerializedName("declaration_number")
        val declarationCode: String? = null
    ) : Receipt()

    data class Item_ffd2(
        @SerializedName("name")
        val name: String,
        @SerializedName("price")
        val price: Double,
        @SerializedName("quantity")
        val quantity: Double,
        @SerializedName("sum")
        val sum: Double?,
        @SerializedName("measure")
        val measure: Int,
        @SerializedName("payment_method")
        val paymentMethod: ReceiptPaymentMethod,
        @SerializedName("payment_object")
        val paymentObject: Int,
        @SerializedName("vat")
        val vat: Vat,
        @SerializedName("agent_info")
        val agentInfo: AgentInfo? = null,
        @SerializedName("supplier_info")
        val supplierInfo: SupplierInfo? = null,
        @SerializedName("user_data")
        val userData: String? = null,
        @SerializedName("excise")
        val excise: Double? = null,
        @SerializedName("country_code")
        val countryCode: String? = null,
        @SerializedName("declaration_number")
        val declarationCode: String? = null,
        @SerializedName("mark_quantity")
        val markQuantity: MarkQuantity? = null,
        @SerializedName("mark_processing_mode")
        val markProcessingMode: String? = null,
        @SerializedName("sectoral_item_props")
        val sectoralItemProps: List<SectoralProp>? = null,
        @SerializedName("mark_code")
        val markCode: MarkCode? = null
    ) : Receipt()
}

data class MarkQuantity(
    @SerializedName("numerator")
    val numerator: Int? = null,
    @SerializedName("denominator")
    val denominator: Int,
)

data class MarkCode(
    @SerializedName("unknown")
    val unknown: String? = null,
    @SerializedName("ean")
    val ean: String? = null,
    @SerializedName("ean13")
    val ean13: String? = null,
    @SerializedName("itf14")
    val itf14: String? = null,
    @SerializedName("gs10")
    val gs10: String? = null,
    @SerializedName("gs1m")
    val gs1m: String? = null,
    @SerializedName("short")
    val short: String? = null,
    @SerializedName("fur")
    val fur: String? = null,
    @SerializedName("egais20")
    val egais20: String? = null,
    @SerializedName("egais30")
    val egais30: String? = null,
)

data class Vat(
    @SerializedName("type")
    val type: VatType,
    @SerializedName("sum")
    val sum: Double? = null
)

data class Payment(
    @SerializedName("type")
    val type: Int,
    @SerializedName("sum")
    val sum: Double?
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

