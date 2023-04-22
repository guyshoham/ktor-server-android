package shoham.guy.ktorserverandroid

import java.security.KeyStore
import java.security.KeyStore.getDefaultType

object SSLCredentials {
    private const val AliasName = "name"
    private const val PASSWORD = "password"

    fun getKeyStore(): KeyStore {
        return KeyStore.getInstance(getDefaultType()).apply {
            val context = MyApplication.INSTANCE
            context.assets.open("keystore.bks").use {
                load(it, getKeyPassword().toCharArray())
            }
        }
    }

    fun getKeyAlias(): String = AliasName
    fun getKeyPassword(): String = PASSWORD
    fun getAliasPassword(): String = PASSWORD
}
