package com.tunehub.app.auth

import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.Base64
import javax.crypto.Cipher

object AuthCrypto {
    fun encryptPassword(
        password: String,
        nonce: String,
        timestamp: Long,
        publicKeyPem: String,
    ): String {
        val payload = "$password.$nonce.$timestamp"
        val publicKey = parsePublicKey(publicKeyPem)
        val cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        val encrypted = cipher.doFinal(payload.toByteArray())
        return Base64.getEncoder().encodeToString(encrypted)
    }

    private fun parsePublicKey(pem: String): PublicKey {
        val cleaned = pem
            .replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")
            .replace("\n", "")
            .trim()
        val decoded = Base64.getDecoder().decode(cleaned)
        val spec = X509EncodedKeySpec(decoded)
        return KeyFactory.getInstance("RSA").generatePublic(spec)
    }
}
