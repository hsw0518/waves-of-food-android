package com.example.myapplication

internal object LoginInputValidator {
    private val email = Regex("^[^\\s@.]+(?:\\.[^\\s@.]+)*@(?:[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?\\.)+[A-Za-z]{2,63}$")
    private val phone = Regex("^\\+?[0-9]{7,15}$")
    private val phoneSeparators = Regex("[\\s().\\-]")

    fun isValidIdentifier(value: String): Boolean {
        val input = value.trim()
        return email.matches(input) ||
            phone.matches(input.replace(phoneSeparators, ""))
    }
}
