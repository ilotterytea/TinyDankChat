package com.flxrs.dankchat.chat

import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class ChatEncryption {
    enum class Encoding {
        Nothing,
        Hebrew,
        Chinese;
    }

    companion object {
        private fun sha256Key(password: String): ByteArray {
            val digest = MessageDigest.getInstance("SHA-256")
            return digest.digest(password.toByteArray(Charsets.UTF_8))
        }

        fun encrypt(text: String, password: String, encoding: Encoding): String {
            val key = sha256Key(password)
            val iv = ByteArray(16)
            SecureRandom().nextBytes(iv)

            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(key, "AES"), IvParameterSpec(iv))
            val encrypted = cipher.doFinal(text.toByteArray(Charsets.UTF_8))

            val combined = iv + encrypted

            val text = combined.joinToString("") { "%02x".format(it) }

            return text.map { encodings[encoding]?.get(it) ?: it.toString() }.joinToString("")
        }

        fun decrypt(input: String, password: String, encoding: Encoding): String {
            val text = input.replace(" ", "")

            // converting to hex
            val map = encodings[encoding]?.entries?.associate { it.value to it.key }
                ?: encodings[Encoding.Nothing]!!.entries.associate { it.value to it.key }
            var hex = ""
            var i = 0
            while (i < text.length) {
                var matched = false
                for ((symbol, key) in map) {
                    if (text.startsWith(symbol, i)) {
                        hex += key
                        i += symbol.length
                        matched = true
                        break
                    }
                }
                if (!matched) throw IllegalArgumentException("Unknown symbol at position $i")
            }

            // decrypting
            val bytes = hex.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
            if (bytes.size < 16) throw IllegalArgumentException("Invalid ciphertext")

            val iv = bytes.copyOfRange(0, 16)
            val ciphertext = bytes.copyOfRange(16, bytes.size)
            val key = sha256Key(password)

            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(key, "AES"), IvParameterSpec(iv))
            return cipher.doFinal(ciphertext).toString(Charsets.UTF_8)
        }

        fun addRandomSpaces(text: String): String {
            val sb = StringBuilder()
            var i = 0
            val random = java.util.Random()

            while (i < text.length) {
                val codePoint = text.codePointAt(i)
                val charCount = Character.charCount(codePoint)
                sb.append(String(Character.toChars(codePoint)))

                if (random.nextDouble() < 0.2) {
                    sb.append(' ')
                }

                i += charCount
            }

            return sb.toString()
        }

        fun detectEncoding(text: String): Encoding? {
            var hasHex = true
            var hasHebrew = false
            var hasChinese = false

            var i = 0
            while (i < text.length) {
                val c = text[i]
                if (c.code < 0x80) {
                    if (!c.isDigit() && c.lowercaseChar() !in 'a'..'f') hasHex = false
                    i += 1
                } else {
                    val codePoint = text.codePointAt(i)
                    if (codePoint in 0x0590..0x05FF) {
                        hasHebrew = true; hasHex = false
                    } else if (codePoint in 0x4E00..0x9FFF) {
                        hasChinese = true; hasHex = false
                    } else {
                        return Encoding.Nothing
                    }
                    i += Character.charCount(codePoint)
                }
            }

            return when {
                hasHex                   -> Encoding.Nothing
                hasHebrew && !hasChinese -> Encoding.Hebrew
                hasChinese && !hasHebrew -> Encoding.Chinese
                else -> null
            }
        }

        val DEFAULT_PASSWORD: String = "CHANGE_THIS"

        val encodings: Map<Encoding, Map<Char, String>> = mapOf(
            Encoding.Nothing to mapOf(
                '0' to "0", '1' to "1", '2' to "2", '3' to "3",
                '4' to "4", '5' to "5", '6' to "6", '7' to "7",
                '8' to "8", '9' to "9", 'a' to "a", 'b' to "b",
                'c' to "c", 'd' to "d", 'e' to "e", 'f' to "f",
            ),
            Encoding.Hebrew to mapOf(
                '0' to "א", '1' to "ב", '2' to "ג", '3' to "ד",
                '4' to "ה", '5' to "ו", '6' to "ז", '7' to "ח",
                '8' to "ט", '9' to "י", 'a' to "כ", 'b' to "ל",
                'c' to "מ", 'd' to "נ", 'e' to "ס", 'f' to "ע",
            ),
            Encoding.Chinese to mapOf(
                '0' to "零", '1' to "一", '2' to "二", '3' to "三",
                '4' to "四", '5' to "五", '6' to "六", '7' to "七",
                '8' to "八", '9' to "九", 'a' to "甲", 'b' to "乙",
                'c' to "丙", 'd' to "丁", 'e' to "戊", 'f' to "己",
            )
        )
    }
}
