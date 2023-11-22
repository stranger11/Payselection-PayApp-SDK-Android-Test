package payselection.demo.utils

import android.content.Context
import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import payselection.demo.R

fun TextInputLayout.updateColor(context: Context, isError: Boolean, validText: String, errorText: String) {
    hint = if (isError) errorText else validText
    defaultHintTextColor =
        if (isError) ColorStateList.valueOf(ContextCompat.getColor(context, R.color.error)) else ColorStateList.valueOf(ContextCompat.getColor(context, R.color.gray))
    if (isError) setBackgroundResource(R.drawable.bg_edittext_error) else setBackgroundResource(R.drawable.bg_edit_text)
}

fun TextInputEditText.updateColor(context: Context, isError: Boolean) {
    if (isError) setTextColor(ContextCompat.getColor(context, R.color.error)) else setTextColor(ContextCompat.getColor(context, R.color.black))
}