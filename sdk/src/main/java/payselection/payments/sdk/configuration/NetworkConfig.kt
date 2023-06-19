package payselection.payments.sdk.configuration

import java.util.concurrent.TimeUnit

class NetworkConfig(
    val serverUrl: String = "https://pgw.payselection.com",
    val connectionTimeOut: Long = TimeUnit.SECONDS.toMillis(90),
    val readWriteTimeOut: Long = TimeUnit.SECONDS.toMillis(90)
)