package payselection.payments.sdk.exceptions

import payselection.payments.sdk.api.models.ServerError

class RequestException(val code: String, message: String) : Exception(message) {

    internal constructor(serverError: ServerError) : this(serverError.code.orEmpty(), serverError.description.orEmpty())
}