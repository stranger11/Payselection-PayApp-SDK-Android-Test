package payselection.payments.sdk.models.requests.pay.enum

import com.google.gson.annotations.SerializedName

enum class ReceiptPaymentObject {
    @SerializedName("commodity")
    Commodity,

    @SerializedName("excise")
    Excise,

    @SerializedName("job")
    Job,

    @SerializedName("service")
    Service,

    @SerializedName("gambling_bet")
    GamblingBet,

    @SerializedName("gambling_prize")
    GamblingPrize,

    @SerializedName("lottery")
    Lottery,

    @SerializedName("lottery_prize")
    LotteryPrize,

    @SerializedName("intellectual_activity")
    IntellectualActivity,

    @SerializedName("payment")
    Payment,

    @SerializedName("agent_commission")
    AgentCommission,

    @SerializedName("composite")
    Composite,

    @SerializedName("award")
    Award,

    @SerializedName("another")
    Another,

    @SerializedName("property_right")
    PropertyRight,

    @SerializedName("non-operating_gain")
    NonOperatingGain,

    @SerializedName("insurance_premium")
    InsurancePremium,

    @SerializedName("sales_tax")
    SalesTax,

    @SerializedName("resort_fee")
    ResortFee,

    @SerializedName("deposit")
    Deposit,

    @SerializedName("expense")
    Expense,

    @SerializedName("pension_insurance_ip")
    PensionInsuranceIp,

    @SerializedName("pension_insurance")
    PensionInsurance,

    @SerializedName("medical_insurance_ip")
    MedicalInsuranceIp,

    @SerializedName("medical_insurance")
    MedicalInsurance,

    @SerializedName("social_insurance")
    SocialInsurance,

    @SerializedName("casino_payment")
    CasinoPayment
}