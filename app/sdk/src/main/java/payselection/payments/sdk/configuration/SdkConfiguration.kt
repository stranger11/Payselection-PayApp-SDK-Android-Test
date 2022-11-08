package payselection.payments.sdk.configuration

class SdkConfiguration(
    val publicKey: String,
    val siteId: String,
    val requestKey: String,
    val isDebug: Boolean = false,
    val networkConfig: NetworkConfig = NetworkConfig()
) {
}