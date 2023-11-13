package payselection.demo.utils

import java.util.Calendar

fun validCardNumber(cardNumber: String):Boolean {
    var sum = 0
    for ((index, char) in cardNumber.withIndex()) {
        var digit = Character.digit(char, 10)
        if (index % 2 == 0) {
            digit *= 2
            if (digit > 9) {
                digit -= 9
            }
        }
        sum += digit
    }
    return sum % 10 == 0 && cardNumber.length == 16 || cardNumber.isEmpty()
}

fun validCardDate(date: String):Boolean {
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
    return  isValid || date.isEmpty()
}

fun validCvv(cvv: String):Boolean {
    return cvv.length == 3 || cvv.isEmpty()
}