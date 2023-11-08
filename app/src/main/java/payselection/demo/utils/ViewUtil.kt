package payselection.demo.utils

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import androidx.annotation.RequiresApi
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

@RequiresApi(Build.VERSION_CODES.M)
fun TextInputLayout.updateColor(context: Context, isError: Boolean, validText: String, errorText: String) {
    hint = if (isError) errorText else validText
    defaultHintTextColor =
        if (isError) ColorStateList.valueOf(context.getColor(R.color.error)) else ColorStateList.valueOf(context.getColor(R.color.gray))
    if (isError) setBackgroundResource(R.drawable.bg_edittext_error) else setBackgroundResource(R.drawable.bg_edit_text)
}

@RequiresApi(Build.VERSION_CODES.M)
fun TextInputEditText.updateColor(context: Context, isError: Boolean) {
    if (isError) setTextColor(context.getColor(R.color.error)) else setTextColor(context.getColor(R.color.black))
}