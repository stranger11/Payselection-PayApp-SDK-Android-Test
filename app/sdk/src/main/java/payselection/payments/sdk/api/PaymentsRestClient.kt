package payselection.payments.sdk.api

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer
import payselection.payments.sdk.api.models.TransactionStatusObject
import payselection.payments.sdk.api.utils.safeApiResult
import payselection.payments.sdk.configuration.SdkConfiguration
import payselection.payments.sdk.crypto.HMAC
import payselection.payments.sdk.models.results.confirm.ConfirmResult
import payselection.payments.sdk.models.results.pay.PaymentResult
import payselection.payments.sdk.utils.Result
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.concurrent.TimeUnit

internal class PaymentsRestClient constructor(
    private val sdkConfiguration: SdkConfiguration
) : PaymentsRestApi {

    private val paymentsRestApi: PaymentsApiFunctions

    init {
        var httpClient = OkHttpClient.Builder()
        httpClient
            .connectTimeout(sdkConfiguration.networkConfig.connectionTimeOut, TimeUnit.MILLISECONDS)
            .readTimeout(sdkConfiguration.networkConfig.readWriteTimeOut, TimeUnit.MILLISECONDS)
            .writeTimeout(sdkConfiguration.networkConfig.readWriteTimeOut, TimeUnit.MILLISECONDS)

        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .method(original.method, original.body)

            val id = UUID.randomUUID().toString()
            request.addHeader("X-SITE-ID", sdkConfiguration.siteId)
            request.addHeader("X-REQUEST-ID", id)
            request.addHeader("X-REQUEST-SIGNATURE", generateSignature(id, original))

            chain.proceed(request.build())
        }
        if (sdkConfiguration.isDebug) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            httpClient = httpClient.addInterceptor(interceptor)
        }
        val gson = GsonBuilder()
            .setLenient()
            .create()
        var retrofit = Retrofit.Builder()
            .baseUrl(sdkConfiguration.networkConfig.serverUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(httpClient.build())

        paymentsRestApi = retrofit.build().create(PaymentsApiFunctions::class.java)
    }

    private fun getBody(requestBody: RequestBody?): String {
        if (requestBody == null)
            return ""
        val buffer = Buffer()
        requestBody.writeTo(buffer)

        val contentType = requestBody.contentType()
        val charset: Charset = contentType?.charset(StandardCharsets.UTF_8) ?: StandardCharsets.UTF_8

        return buffer.readString(charset)
    }

    private fun generateSignature(id: String, original: Request): String {
        val signatureValue = original.method + "\n" +
                original.url.toString().removePrefix(sdkConfiguration.networkConfig.serverUrl) + "\n" +
                sdkConfiguration.siteId + "\n" +
                id + "\n" +
                getBody(original.body)
        return HMAC.hash(
            type = "HmacSHA256",
            key = sdkConfiguration.requestKey,
            value = signatureValue
        )
    }

    override suspend fun pay(data: JsonObject): Result<PaymentResult> = safeApiResult {
        paymentsRestApi.pay(data).await()
    }

    override suspend fun getOrderStatus(orderId: String): Result<List<TransactionStatusObject>> = safeApiResult {
        paymentsRestApi.getOrderStatus(orderId).await()
    }

    override suspend fun getTransaction(transactionId: String): Result<TransactionStatusObject> = safeApiResult {
        paymentsRestApi.getTransaction(transactionId).await()
    }

    override suspend fun refund(data: JsonObject): Result<PaymentResult> = safeApiResult {
        paymentsRestApi.refund(data).await()
    }

    override suspend fun confirm(data: JsonObject): Result<ConfirmResult> = safeApiResult {
        paymentsRestApi.confirm(data).await()
    }
}
