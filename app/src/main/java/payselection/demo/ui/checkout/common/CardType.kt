package payselection.demo.ui.checkout.common

import payselection.demo.R

enum class CardType(val title:Int, val image: Int, val imageWithLine: Int) {
    VISA(R.string.visa, R.drawable.ic_visa, R.drawable.ic_visa_with_line),
    MASTERCARD(R.string.mastercard, R.drawable.ic_mastercard, R.drawable.ic_mastercard_with_line),
    MIR(R.string.mir, R.drawable.ic_mir, R.drawable.ic_mir_with_line)
}