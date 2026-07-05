package com.example.chefstable.util

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText

class PhoneMaskWatcher(private val editText: TextInputEditText) : TextWatcher {

    private var isUpdating = false
    private var oldText = ""

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        if (!isUpdating) {
            oldText = s?.toString() ?: ""
        }
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        if (isUpdating) return

        val text = s?.toString()?.replace(Regex("[^0-9+]"), "") ?: ""

        // Strip leading +7 or 8 to get raw digits
        val digits = when {
            text.startsWith("+7") -> text.substring(2)
            text.startsWith("8") && text.length > 1 -> text.substring(1)
            text.startsWith("7") && text.length > 1 -> text.substring(1)
            else -> text.replace("+", "")
        }.take(10)

        isUpdating = true

        val formatted = buildString {
            append("+7")
            if (digits.isNotEmpty()) {
                append(" (")
                append(digits.take(3))
            }
            if (digits.length > 3) {
                append(") ")
                append(digits.substring(3, minOf(digits.length, 6)))
            }
            if (digits.length > 6) {
                append("-")
                append(digits.substring(6, minOf(digits.length, 8)))
            }
            if (digits.length > 8) {
                append("-")
                append(digits.substring(8, minOf(digits.length, 10)))
            }
        }

        editText.setText(formatted)
        editText.setSelection(formatted.length)

        isUpdating = false
    }

    companion object {
        fun applyTo(editText: TextInputEditText) {
            editText.addTextChangedListener(PhoneMaskWatcher(editText))
        }

        fun extractRawNumber(text: String): String {
            val digits = text.replace(Regex("[^0-9]"), "")
            return when {
                digits.length == 11 && digits.startsWith("7") -> "+$digits"
                digits.length == 10 -> "+7$digits"
                digits.length > 11 -> "+" + digits.takeLast(10)
                else -> "+7" + digits.takeLast(10)
            }
        }
    }
}
