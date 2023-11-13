package payselection.demo.utils

import android.content.Context
import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import payselection.demo.R

class CombineLiveData<F, S, R>(first: LiveData<F>, second: LiveData<S>, combine: (F?, S?) -> R) :
    MediatorLiveData<R>() {

    init {
        addSource(first) {
            value = combine(it, second.value)
        }
        addSource(second) {
            value = combine(first.value, it)
        }
    }
}

class CombineTripleLiveData<F, S, T, R>(first: LiveData<F>, second: LiveData<S>, third: LiveData<T>, combine: (F?, S?, T?) -> R) : MediatorLiveData<R>() {

    init {
        addSource(first) {
            value = combine(it, second.value, third.value)
        }
        addSource(second) {
            value = combine(first.value, it, third.value)
        }
        addSource(third) {
            value = combine(first.value, second.value, it)
        }
    }
}

fun TextInputLayout.updateColor(context: Context, isError: Boolean, validText: String, errorText: String) {
    hint = if (isError) errorText else validText
    defaultHintTextColor =
        if (isError) ColorStateList.valueOf(ContextCompat.getColor(context, R.color.error)) else ColorStateList.valueOf(ContextCompat.getColor(context, R.color.gray))
    if (isError) setBackgroundResource(R.drawable.bg_edittext_error) else setBackgroundResource(R.drawable.bg_edit_text)
}

fun TextInputEditText.updateColor(context: Context, isError: Boolean) {
    if (isError) setTextColor(ContextCompat.getColor(context, R.color.error)) else setTextColor(ContextCompat.getColor(context, R.color.black))
}

fun String.matchesCardNumber(regex: String): Boolean {
    val cardNumber = filter { it.isDigit() }
    return Regex(regex).matches(cardNumber)
}