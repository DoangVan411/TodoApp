package com.example.todoapp.utils

import java.security.MessageDigest
import java.security.SecureRandom

class Security {
    companion object {
        fun generateSalt(): String {
            val random = SecureRandom()
            val salt = ByteArray(16)
            random.nextBytes(salt)
            return salt.joinToString("") {"%02x".format(it)}
        }

        fun hashPassword(password: String, salt: String): String {
            val digest = MessageDigest.getInstance("SHA-256")
            val hashedBytes = digest.digest((password + salt).toByteArray())
            return hashedBytes.joinToString("") { "%02x".format(it) }
        }
    }
}