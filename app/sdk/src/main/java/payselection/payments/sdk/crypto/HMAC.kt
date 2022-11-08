package payselection.payments.sdk.crypto

import android.util.Log
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object HMAC {

    fun hash(type: String?, value: String, key: String): String {
        try {
            val mac = Mac.getInstance(type)
            val secret = SecretKeySpec(key.toByteArray(), type)
            mac.init(secret)
            val digest = mac.doFinal(value.toByteArray())
            val sb = StringBuilder(digest.size * 2)
            var s: String
            for (b in digest) {
                s = Integer.toHexString(b.toInt()).takeLast(2)
                if (s.length == 1) sb.append('0')
                sb.append(s)
            }
            return sb.toString()
        } catch (e: Exception) {
            Log.v("TAG", "Exception [" + e.message + "]", e)
        }
        return ""
    }

    fun hash(type: String?, value: String, key: ByteArray): String {
        try {
            val mac = Mac.getInstance(type)
            val secret = SecretKeySpec(key, type)
            mac.init(secret)
            val digest = mac.doFinal(value.toByteArray())
            val sb = StringBuilder(digest.size * 2)
            var s: String
            for (b in digest) {
                s = Integer.toHexString(b.toInt()).takeLast(2)
                if (s.length == 1) sb.append('0')
                sb.append(s)
            }
            return sb.toString()
        } catch (e: Exception) {
            Log.v("TAG", "Exception [" + e.message + "]", e)
        }
        return ""
    }
}