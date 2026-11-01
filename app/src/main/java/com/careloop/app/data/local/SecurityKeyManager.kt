package com.careloop.app.data.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import java.security.SecureRandom

object SecurityKeyManager {
    private const val PREFS_FILE = "careloop_secure_keys"
    private const val KEY_DB_PASSPHRASE = "careloop_db_passphrase_bytes"

    fun getOrCreatePassphrase(context: Context): ByteArray {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        val sharedPreferences = EncryptedSharedPreferences.create(
            PREFS_FILE,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        var passphraseHex = sharedPreferences.getString(KEY_DB_PASSPHRASE, null)
        if (passphraseHex == null) {
            val randomBytes = ByteArray(32)
            SecureRandom().nextBytes(randomBytes)
            passphraseHex = randomBytes.joinToString("") { "%02x".format(it) }
            sharedPreferences.edit().putString(KEY_DB_PASSPHRASE, passphraseHex).apply()
        }

        return passphraseHex.toByteArray(Charsets.UTF_8)
    }
}

