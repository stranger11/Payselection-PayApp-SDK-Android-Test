package payselection.payments.sdk.crypto

internal class EncryptedData(
    val encrypted: ByteArray,
    val key: ByteArray,
    val iv: ByteArray,
    val tag: ByteArray
) {
}