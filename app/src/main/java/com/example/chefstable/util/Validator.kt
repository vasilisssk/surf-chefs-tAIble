package com.example.chefstable.util

import java.util.regex.Pattern

object Validator {

    private val EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    )

    private val PHONE_PATTERN = Pattern.compile(
        "^\\+7\\d{10}$"
    )

    private val NAME_PATTERN = Pattern.compile(
        "^[А-Яа-яA-Za-z\\s]{2,25}$"
    )

    fun validateName(name: String): String? {
        if (name.isBlank()) return "Имя обязательно"
        if (!NAME_PATTERN.matcher(name).matches()) return "Имя имеет неверный формат"
        return null
    }

    fun validateEmail(email: String): String? {
        if (email.isBlank()) return "Email обязателен"
        if (!EMAIL_PATTERN.matcher(email).matches()) return "Email имеет неверный формат"
        return null
    }

    fun validatePhone(phone: String): String? {
        if (phone.isBlank()) return "Телефон обязателен"
        if (!PHONE_PATTERN.matcher(phone).matches()) return "Телефон имеет неверный формат"
        return null
    }

    fun validatePassword(password: String): String? {
        if (password.isBlank()) return "Пароль не может быть пустым"
        if (password.length < 8) return "Пароль слишком короткий"
        return null
    }

    fun validateLoginPassword(password: String): String? {
        if (password.isBlank()) return "Пароль не может быть пустым"
        return null
    }
}
