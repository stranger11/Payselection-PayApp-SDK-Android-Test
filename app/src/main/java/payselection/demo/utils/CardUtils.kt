package payselection.demo.utils

import payselection.demo.ui.checkout.CheckoutViewModel
import payselection.demo.ui.checkout.common.CardType
import java.util.Calendar

fun String.luhnAlgorithm() = reversed()
    .map(Character::getNumericValue)
    .mapIndexed { index, digit ->
        when {
            index % 2 == 0 -> digit
            digit < 5 -> digit * 2
            else -> digit * 2 - 9
        }
    }.sum() % 10 == 0

fun isValidCardNumber(cardNumber: String): Boolean {
    return if (cardNumber.isEmpty()) {
        true
    } else if (cardNumber.length != CARD_NUMBER_LENGTH) {
        false
    } else {
        cardNumber.luhnAlgorithm()
    }
}

fun isValidCardDate(date: String): Boolean {
    val isValid =
        if (date.length == 5 && date.indexOf('/') == 2) {
            val month = date.substring(0, 2).toInt()
            val year = date.substring(3).toInt()
            val allowedDate = Calendar.getInstance()
            allowedDate.set(2022, 1, 1)
            val inputDate = Calendar.getInstance()
            inputDate.set(2000 + year, month, 1)

            month in 1..12 && inputDate >= allowedDate
        } else {
            false
        }
    return isValid || date.isEmpty()
}

fun isValidCvv(cvv: String): Boolean {
    return cvv.length == CARD_CVV_LENGTH || cvv.isEmpty()
}

fun getPaymentSystem(cardNumber: String): CardType? = when {
    cardNumber.startsWith("4") -> CardType.VISA
    cardNumber.startsWith("51") || cardNumber.startsWith("52") || cardNumber.startsWith("53")
            || cardNumber.startsWith("54") || cardNumber.startsWith("55") -> CardType.MASTERCARD

    cardNumber.startsWith("2") -> CardType.MIR
    else -> null
}

val MASTERCARD_REGEX = Regex("^5[1-5][0-9]{14}$")
val VISA_REGEX = Regex("^4[0-9]{12}(?:[0-9]{3})?$")
val MIR_REGEX = Regex("^2[0-9]{15}$")

val CARD_NUMBER_LENGTH = 16
val CARD_DATE_LENGTH = 5
val CARD_CVV_LENGTH = 3