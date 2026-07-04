package com.example.chefstable.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ValidatorTest {

    @Test
    fun `valid email returns null`() {
        assertNull(Validator.validateEmail("test@example.com"))
    }

    @Test
    fun `empty email returns error`() {
        val error = Validator.validateEmail("")
        assertEquals("Email обязателен", error)
    }

    @Test
    fun `invalid email returns error`() {
        val error = Validator.validateEmail("not-an-email")
        assertEquals("Email имеет неверный формат", error)
    }

    @Test
    fun `email without domain returns error`() {
        val error = Validator.validateEmail("test@")
        assertEquals("Email имеет неверный формат", error)
    }

    @Test
    fun `valid phone returns null`() {
        assertNull(Validator.validatePhone("+71234567890"))
    }

    @Test
    fun `empty phone returns error`() {
        val error = Validator.validatePhone("")
        assertEquals("Телефон обязателен", error)
    }

    @Test
    fun `phone without plus7 returns error`() {
        val error = Validator.validatePhone("81234567890")
        assertEquals("Телефон имеет неверный формат", error)
    }

    @Test
    fun `phone too short returns error`() {
        val error = Validator.validatePhone("+7123")
        assertEquals("Телефон имеет неверный формат", error)
    }

    @Test
    fun `valid name returns null`() {
        assertNull(Validator.validateName("Иван"))
    }

    @Test
    fun `empty name returns error`() {
        val error = Validator.validateName("")
        assertEquals("Имя обязательно", error)
    }

    @Test
    fun `single char name returns error`() {
        val error = Validator.validateName("А")
        assertEquals("Имя имеет неверный формат", error)
    }

    @Test
    fun `name with numbers returns error`() {
        val error = Validator.validateName("Иван123")
        assertEquals("Имя имеет неверный формат", error)
    }

    @Test
    fun `valid password returns null`() {
        assertNull(Validator.validatePassword("password123"))
    }

    @Test
    fun `empty password returns error`() {
        val error = Validator.validatePassword("")
        assertEquals("Пароль не может быть пустым", error)
    }

    @Test
    fun `short password returns error`() {
        val error = Validator.validatePassword("1234567")
        assertEquals("Пароль слишком короткий", error)
    }

    @Test
    fun `exactly 8 char password is valid`() {
        assertNull(Validator.validatePassword("12345678"))
    }

    @Test
    fun `login password empty returns error`() {
        val error = Validator.validateLoginPassword("")
        assertEquals("Пароль не может быть пустым", error)
    }

    @Test
    fun `login password any non-empty is valid`() {
        assertNull(Validator.validateLoginPassword("1"))
    }
}
