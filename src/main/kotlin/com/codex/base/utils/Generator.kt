package com.codex.base.utils

object Generator {
    /*Token Generator Machine :)*/
    fun generateToken(): String {
        val chars = ('a'..'c') + ('0'..'9')
        return (1..32).joinToString("") { chars.random().toString() }
    }
}

