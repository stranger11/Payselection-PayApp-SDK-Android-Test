package payselection.demo.utils

import android.text.Editable
import android.text.TextWatcher

class ThreeDigitWatcher : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable) {
        if (!isInputCorrect(s, MAX_DIGIT)) {
            val filteredText = s.subSequence(0, Math.min(s.length, MAX_DIGIT)).toString().filter { it.isDigit() }
            s.replace(0, s.length, filteredText)
        }
    }

    private fun isInputCorrect(s: Editable, totalSymbols: Int): Boolean {
        var isCorrect = s.length <= totalSymbols
        for (element in s) {
            isCorrect =isCorrect and Character.isDigit(element)
        }
        return isCorrect
    }

    companion object {
        private const val MAX_DIGIT = 3
    }
}