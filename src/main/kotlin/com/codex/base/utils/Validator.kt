package com.codex.base.utils

object Validator {
    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
    private val phoneRegex = Regex("^\\+?[0-9]{10,15}$")

    fun isValidEmail(input: String): Boolean {
        return emailRegex.matches(input)
    }

    fun isValidPhone(input: String): Boolean {
        return phoneRegex.matches(input)
    }
}
