package com.codex.base.utils;

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.*

object PasswordOperations {
    // Specify the key length (128, 192, or 256 bits)
    private const val KEY_LENGTH = 128 // or 192 or 256
    private const val ALGORITHM = "SHA-256"

    fun hashPassword(password: String): String {
        val secureRandom = SecureRandom()
        val salt = ByteArray(KEY_LENGTH).also(secureRandom::setSeed)
        val messageDigest = MessageDigest.getInstance(ALGORITHM)
        messageDigest.update(salt)
        val digestPassword = messageDigest.digest(password.toByteArray(StandardCharsets.UTF_8))
        return Base64.getEncoder().encodeToString(digestPassword)
    }

    fun verifyPassword(password: String, storedPassword: String): Boolean {
        val salt = ByteArray(KEY_LENGTH)
        val messageDigest = MessageDigest.getInstance(ALGORITHM)
        messageDigest.update(salt)
        val digestedPassword = messageDigest.digest(password.toByteArray(StandardCharsets.UTF_8))
        val decodedPassword = Base64.getDecoder().decode(storedPassword)
        return MessageDigest.isEqual(digestedPassword, decodedPassword)
    }
}