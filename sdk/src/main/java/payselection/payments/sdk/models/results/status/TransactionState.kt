package payselection.payments.sdk.models.results.status

enum class TransactionState(val code: String) {
    Success("success"),
    Preauthorized("preauthorized"),
    Pending("pending"),
    Voided("voided"),
    Declined("declined"),
    WaitFor3ds("wait_for_3ds"),
    Redirect("redirect"),
    Unknown("");

    companion object {

        fun convert(state: String?): TransactionState {
            return values().find { it.code == state } ?: Unknown
        }
    }
}

//success
//pending
//voided
//declined
//wait_for_3ds
//redirect
//preauthorized